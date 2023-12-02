package com.ssonsal.football.review.controller;

import com.ssonsal.football.review.dto.request.ReviewRequestDto;
import com.ssonsal.football.review.dto.response.ReviewResponseDto;
import com.ssonsal.football.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewService reviewService;
    @PostMapping
    public ResponseEntity<ReviewResponseDto> createReview(
            @RequestBody ReviewRequestDto reviewRequestDto) {
        ReviewResponseDto createdReview = reviewService.createReview(reviewRequestDto);
        return ResponseEntity.ok(createdReview);
    }

    // 검색한 팀아이디에 대한 리뷰들 조회
    @GetMapping("/team/{teamId}")
    public ResponseEntity<List<ReviewResponseDto>> getTeamReview(@PathVariable("teamId") Long teamId){
        log.info(String.valueOf(teamId));
        List<ReviewResponseDto> result = reviewService.teamReviewList(teamId);
        return ResponseEntity.ok(result);
    }

    // 검색한 용병아이디에 대한 리뷰들 조회
    @GetMapping("/sub/{userId}")
    public ResponseEntity<List<ReviewResponseDto>> getUserReview(@PathVariable("userId") Long userId){
        log.info(String.valueOf(userId));
        List<ReviewResponseDto> result = reviewService.userReviewList(userId);
        return ResponseEntity.ok(result);
    }
}
