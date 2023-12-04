package com.ssonsal.football.review.controller;

import com.ssonsal.football.global.util.SuccessCode;
import com.ssonsal.football.global.util.formatter.DataResponseBodyFormatter;
import com.ssonsal.football.global.util.formatter.ResponseBodyFormatter;
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

    // 리뷰 생성
    @PostMapping
    public ResponseEntity<ResponseBodyFormatter> createReview(
            @RequestBody ReviewRequestDto reviewRequestDto) {
        ReviewResponseDto createdReview = reviewService.createReview(reviewRequestDto);
        return DataResponseBodyFormatter.put(SuccessCode.SUCCESS, createdReview);
    }

    // 검색한 팀아이디에 대한 리뷰들 조회
    @GetMapping("/team/{teamId}")
    public ResponseEntity<ResponseBodyFormatter> getTeamReview(@PathVariable("teamId") Long teamId){
        log.info(String.valueOf(teamId));
        List<ReviewResponseDto> result = reviewService.teamReviewList(teamId);
        return DataResponseBodyFormatter.put(SuccessCode.SUCCESS, result);
    }

    // 검색한 용병아이디에 대한 리뷰들 조회
    @GetMapping("/sub/{userId}")
    public ResponseEntity<ResponseBodyFormatter> getUserReview(@PathVariable("userId") Long userId){
        log.info(String.valueOf(userId));
        List<ReviewResponseDto> result = reviewService.userReviewList(userId);
        return DataResponseBodyFormatter.put(SuccessCode.SUCCESS, result);
    }

    // 삭제코드 변경
    @PutMapping("/{reviewId}/update")
    public ResponseEntity<ResponseBodyFormatter> updateDeleteCode(
            @PathVariable Long reviewId,
            @RequestParam(name = "deleteCode", required = false) Integer deleteCode) {
        reviewService.updateDeleteCode(reviewId, deleteCode);
        return DataResponseBodyFormatter.put(SuccessCode.SUCCESS);
    }
}
