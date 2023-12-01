package com.ssonsal.football.review.service;

import com.ssonsal.football.review.dto.request.ReviewRequestDto;
import com.ssonsal.football.review.dto.response.ReviewResponseDto;

import java.util.List;

public interface ReviewService {
    ReviewResponseDto createReview(ReviewRequestDto reviewRequestDto);

    List<ReviewResponseDto> userReviewList(Long userId);

    List<ReviewResponseDto> teamReviewList(Long teamId);
}
