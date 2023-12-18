package com.ssonsal.football.review.service;

import com.ssonsal.football.review.dto.request.ReviewRequestDto;
import com.ssonsal.football.review.dto.response.ReviewListResponseDto;
import com.ssonsal.football.review.dto.response.ReviewResponseDto;
import com.ssonsal.football.review.dto.response.ScoreResponseDto;

import java.util.List;

public interface ReviewService {
    ReviewResponseDto createReview(ReviewRequestDto reviewRequestDto, Long userId);

    List<ReviewListResponseDto> userReviewList(Long userId);

    List<ReviewListResponseDto> teamReviewList(Long teamId);

    void updateDeleteCode(Long reviewId, Integer deleteCode);

    ReviewResponseDto getReview(Long reviewId);

    ScoreResponseDto subAvgScore(Long userId);

    ScoreResponseDto teamAvgScore(Long teamId);

    void updateSubAvgScore(ScoreResponseDto scoreResponseDto, Long userId);

    void updateTeamAvgScore(ScoreResponseDto scoreResponseDto, Long teamId);
}
