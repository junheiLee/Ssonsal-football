package com.ssonsal.football.review.service;

import com.ssonsal.football.game.entity.Game;
import com.ssonsal.football.game.repository.GameRepository;
import com.ssonsal.football.global.exception.CustomException;
import com.ssonsal.football.global.util.ErrorCode;
import com.ssonsal.football.review.dto.request.ReviewRequestDto;
import com.ssonsal.football.review.dto.response.ReviewListResponseDto;
import com.ssonsal.football.review.dto.response.ReviewResponseDto;
import com.ssonsal.football.review.dto.response.ScoreResponseDto;
import com.ssonsal.football.review.entity.Review;
import com.ssonsal.football.review.exception.ReviewErrorCode;
import com.ssonsal.football.review.repository.ReviewRepository;
import com.ssonsal.football.team.entity.Team;
import com.ssonsal.football.team.repository.TeamRepository;
import com.ssonsal.football.user.entity.User;
import com.ssonsal.football.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final GameRepository gameRepository;
    private final TeamRepository teamRepository;

    @Transactional
    public ReviewResponseDto createReview(ReviewRequestDto reviewRequestDto, Long userId) {
        // 해당하는 게임ID와 유저ID 가져오기
        System.out.println("여긴서비스임플->" + reviewRequestDto.toString());
        reviewRequestDto.setWriterId(userId);

        Game game = gameRepository.findById(reviewRequestDto.getGameId())
                .orElseThrow(() -> new CustomException(ReviewErrorCode.GAME_NOT_FOUND));

        User user = userRepository.findById(reviewRequestDto.getWriterId())
                .orElseThrow(() -> new CustomException(ReviewErrorCode.USER_NOT_FOUND));

        Review review = Review.builder()
                .game(game)
                .user(user)
                .reviewCode(reviewRequestDto.getReviewCode())
                .targetId(reviewRequestDto.getTargetId())
                .mannerScore(reviewRequestDto.getMannerScore())
                .skillScore(reviewRequestDto.getSkillScore())
                .comment(reviewRequestDto.getComment())
                .build();

        reviewRepository.save(review);

        if (reviewRequestDto.getReviewCode() == 1) {
            updateSubAvgScore(subAvgScore(reviewRequestDto.getTargetId()), reviewRequestDto.getTargetId());
        } else if (reviewRequestDto.getReviewCode() == 2) {
            updateTeamAvgScore(teamAvgScore(reviewRequestDto.getTargetId()), reviewRequestDto.getTargetId());
        }

        return ReviewResponseDto.fromEntity(review);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReviewListResponseDto> teamReviewList(Long teamId) {
        List<Review> reviews = reviewRepository.findReviewsByTeamId(teamId);

        List<ReviewListResponseDto> teamReviews = new ArrayList<>();
        for (Review review : reviews) {
            teamReviews.add(ReviewListResponseDto.fromEntity(review));
        }

        return teamReviews;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReviewListResponseDto> userReviewList(Long userId) {
        List<Review> reviews = reviewRepository.findReviewsByUserId(userId);

        List<ReviewListResponseDto> userReviews = new ArrayList<>();
        for (Review review : reviews) {
            userReviews.add(ReviewListResponseDto.fromEntity(review));
        }

        return userReviews;
    }

    @Transactional
    public void updateDeleteCode(Long reviewId, Integer deleteCode) {
        reviewRepository.findById(reviewId)
                .orElseThrow(() -> new CustomException(ReviewErrorCode.REVIEW_NOT_FOUND));

        if (!(deleteCode == 0 || deleteCode == 1)) {
            throw new CustomException(ReviewErrorCode.STATUS_ERROR);
        }

        reviewRepository.updateDeleteCode(reviewId, deleteCode);
    }

    @Override
    @Transactional(readOnly = true)
    public ReviewResponseDto getReview(Long reviewId) {
        reviewRepository.findById(reviewId)
                .orElseThrow(() -> new CustomException(ReviewErrorCode.REVIEW_NOT_FOUND));

        return ReviewResponseDto.fromEntity(reviewRepository.findReviewById(reviewId));
    }

    @Override
    public ScoreResponseDto subAvgScore(Long userId) {
        List<Review> reviews = reviewRepository.findReviewsByUserId(userId);

        if (reviews.isEmpty()) {
            log.error("해당하는 리뷰가 없습니다.");
            throw new CustomException(ReviewErrorCode.REVIEW_NOT_FOUND);
        }

        float totalMannerScore = 0.0f;
        float totalSkillScore = 0.0f;

        for (Review review : reviews) {
            totalMannerScore += review.getMannerScore();
            totalSkillScore += review.getSkillScore();
        }

        float avgMannerScore = totalMannerScore / reviews.size();
        float avgSkillScore = totalSkillScore / reviews.size();

        ScoreResponseDto subScore = new ScoreResponseDto();
        subScore.setAvgMannerScore(avgMannerScore);
        subScore.setAvgSkillScore(avgSkillScore);

        return subScore;
    }

    @Override
    public ScoreResponseDto teamAvgScore(Long teamId) {
        List<Review> reviews = reviewRepository.findReviewsByTeamId(teamId);

        if (reviews.isEmpty()) {
            log.error("해당하는 리뷰가 없습니다.");
            throw new CustomException(ReviewErrorCode.REVIEW_NOT_FOUND);
        }

        float totalMannerScore = 0.0f;
        float totalSkillScore = 0.0f;

        for (Review review : reviews) {
            totalMannerScore += review.getMannerScore();
            totalSkillScore += review.getSkillScore();
        }

        float avgMannerScore = totalMannerScore / reviews.size();
        float avgSkillScore = totalSkillScore / reviews.size();

        ScoreResponseDto teamScore = new ScoreResponseDto();
        teamScore.setAvgMannerScore(avgMannerScore);
        teamScore.setAvgSkillScore(avgSkillScore);

        return teamScore;
    }

    @Override
    @Transactional
    public void updateSubAvgScore(ScoreResponseDto scoreResponseDto, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND));

        user.updateScore(scoreResponseDto.getAvgMannerScore(), scoreResponseDto.getAvgSkillScore());
    }

    @Override
    @Transactional
    public void updateTeamAvgScore(ScoreResponseDto scoreResponseDto, Long teamId) {
        Team team = teamRepository.findById(teamId).orElseThrow(
                () -> new CustomException(ErrorCode.TEAM_NOT_FOUND));

        team.updateScore(scoreResponseDto.getAvgMannerScore(), scoreResponseDto.getAvgSkillScore());
    }
}