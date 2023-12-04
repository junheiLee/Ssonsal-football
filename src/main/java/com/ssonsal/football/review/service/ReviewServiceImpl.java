package com.ssonsal.football.review.service;

import com.ssonsal.football.game.entity.Game;
import com.ssonsal.football.game.exception.GameErrorCode;
import com.ssonsal.football.game.repository.GameRepository;
import com.ssonsal.football.global.exception.CustomException;
import com.ssonsal.football.review.dto.request.ReviewRequestDto;
import com.ssonsal.football.review.dto.response.ReviewResponseDto;
import com.ssonsal.football.review.etity.Review;
import com.ssonsal.football.review.exception.ReviewErrorCode;
import com.ssonsal.football.review.repository.ReviewRepository;
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
@Transactional(readOnly = true)
@Service
public class ReviewServiceImpl implements ReviewService{

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final GameRepository gameRepository;

    @Transactional
    public ReviewResponseDto createReview(ReviewRequestDto reviewRequestDto) {
        // 해당하는 게임ID와 유저ID 가져오기
        Game game = gameRepository.findById(reviewRequestDto.getGameId())
                .orElseThrow(() -> new CustomException(ReviewErrorCode.GAME_NOT_FOUND));

        User user = userRepository.findById(reviewRequestDto.getWriterId())
                .orElseThrow(() -> new CustomException(ReviewErrorCode.USER_NOT_FOUND));

        qualificationToWrite(game, user);

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

        return ReviewResponseDto.fromEntity(review);
    }

    @Override
    public List<ReviewResponseDto> teamReviewList(Long teamId) {
        List<Review> reviews = reviewRepository.findReviewsByTeamId(teamId);

        List<ReviewResponseDto> teamReviews = new ArrayList<>();
        for (Review review : reviews) {
            teamReviews.add(ReviewResponseDto.fromEntity(review));
        }

        return teamReviews;
    }

    @Override
    public List<ReviewResponseDto> userReviewList(Long userId) {
        List<Review> reviews = reviewRepository.findReviewsByUserId(userId);

        List<ReviewResponseDto> userReviews = new ArrayList<>();
        for (Review review : reviews) {
            userReviews.add(ReviewResponseDto.fromEntity(review));
        }

        return userReviews;
    }

    private void qualificationToWrite(Game game, User user) {
        if (game == null || user == null) {
            log.error("리뷰를 작성할 수 있는 조건이 아닙니다.");
            throw new CustomException(ReviewErrorCode.NO_QUALIFICATION);
        }
    }
}
