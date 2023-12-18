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

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final GameRepository gameRepository;
    private final TeamRepository teamRepository;

    @Transactional
    public ReviewResponseDto createReview(ReviewRequestDto reviewRequestDto, Long userId) {

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
        } else if (reviewRequestDto.getReviewCode() == 0) {
            updateTeamAvgScore(teamAvgScore(reviewRequestDto.getTargetId()), reviewRequestDto.getTargetId());
        }

        return ReviewResponseDto.fromEntity(review);
    }

    @Override
    public List<ReviewListResponseDto> teamReviewList(Long teamId) {

        teamRepository.findById(teamId).orElseThrow(
                () -> new CustomException(ErrorCode.TEAM_NOT_FOUND));

        List<Review> reviews = reviewRepository.findReviewsByTeamId(teamId);

        return reviews.stream()
                .map(ReviewListResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<ReviewListResponseDto> userReviewList(Long userId) {

        userRepository.findById(userId).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND));

        List<Review> reviews = reviewRepository.findReviewsByUserId(userId);

        return reviews.stream()
                .map(ReviewListResponseDto::fromEntity)
                .collect(Collectors.toList());
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
    public ReviewResponseDto getReview(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new CustomException(ReviewErrorCode.REVIEW_NOT_FOUND));

        return ReviewResponseDto.fromEntity(review);
    }

    @Override
    public ScoreResponseDto subAvgScore(Long userId) {
        List<Review> reviews = reviewRepository.findReviewsByUserId(userId);

        if (reviews.isEmpty()) {
            log.error("해당하는 리뷰가 없습니다.");
            throw new CustomException(ReviewErrorCode.REVIEW_NOT_FOUND);
        }

        float avgMannerScore = (float) reviews.stream()
                .mapToDouble(Review::getMannerScore)
                .average()
                .orElse(0.0);

        float avgSkillScore = (float) reviews.stream()
                .mapToDouble(Review::getSkillScore)
                .average()
                .orElse(0.0);

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

        float avgMannerScore = (float) reviews.stream()
                .mapToDouble(Review::getMannerScore)
                .average()
                .orElse(0.0);

        float avgSkillScore = (float) reviews.stream()
                .mapToDouble(Review::getSkillScore)
                .average()
                .orElse(0.0);

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