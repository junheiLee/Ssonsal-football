package com.ssonsal.football.review.service;

import com.ssonsal.football.review.dto.request.ReviewRequestDto;
import com.ssonsal.football.review.dto.response.ReviewListResponseDto;
import com.ssonsal.football.review.dto.response.ReviewResponseDto;

import java.util.List;

public interface ReviewService {
    ReviewResponseDto createReview(ReviewRequestDto reviewRequestDto);

    List<ReviewListResponseDto> userReviewList(Long userId);

    List<ReviewListResponseDto> teamReviewList(Long teamId);

    void updateDeleteCode(Long reviewId, Integer deleteCode);

    ReviewResponseDto getReview(Long reviewId);
}
