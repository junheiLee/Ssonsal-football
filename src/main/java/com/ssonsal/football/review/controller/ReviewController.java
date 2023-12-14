package com.ssonsal.football.review.controller;

import com.ssonsal.football.global.util.SuccessCode;
import com.ssonsal.football.global.util.formatter.DataResponseBodyFormatter;
import com.ssonsal.football.global.util.formatter.ResponseBodyFormatter;
import com.ssonsal.football.review.dto.request.ReviewRequestDto;
import com.ssonsal.football.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    /**
     * 리뷰 생성 시, 호출되는 api
     *
     * @param  reviewRequestDto 리뷰 생성에 필요한 정보
     * @return 성공 코드와 생성된 리뷰를 ResponseBody 에 담아 반환
     */
    @PostMapping
    public ResponseEntity<ResponseBodyFormatter> createReview(
            @RequestBody ReviewRequestDto reviewRequestDto) {

        return DataResponseBodyFormatter.put(SuccessCode.SUCCESS, reviewService.createReview(reviewRequestDto));
    }

    /**
     * 해당 팀에 대한 리뷰 목록 반환 api
     *
     * @param teamId 팀 아이디
     * @return 해당 팀에 대한 리뷰 list 반환
     */
    @GetMapping("/team/{teamId}")
    public ResponseEntity<ResponseBodyFormatter> getTeamReview(@PathVariable("teamId") Long teamId) {
        log.info(String.valueOf(teamId));

        return DataResponseBodyFormatter.put(SuccessCode.SUCCESS, reviewService.teamReviewList(teamId));
    }

    /**
     * 해당 유저에 대한 용병 리뷰 목록 반환 api
     *
     * @param userId 유저 아이디
     * @return 해당 유저에 대한 용병 리뷰 list 반환
     */
    @GetMapping("/sub/{userId}")
    public ResponseEntity<ResponseBodyFormatter> getUserReview(@PathVariable("userId") Long userId) {
        log.info(String.valueOf(userId));

        return DataResponseBodyFormatter.put(SuccessCode.SUCCESS, reviewService.userReviewList(userId));
    }

    /**
     * 해당 리뷰의 삭제 코드를 변경 하는 api
     *
     * @param reviewId 리뷰 아이디
     * @return 성공 메시지 반환
     */
    @PutMapping("/{reviewId}/update")
    public ResponseEntity<ResponseBodyFormatter> updateDeleteCode(
            @PathVariable Long reviewId,
            @RequestParam(name = "deleteCode", required = false) Integer deleteCode) {
        reviewService.updateDeleteCode(reviewId, deleteCode);
        return DataResponseBodyFormatter.put(SuccessCode.SUCCESS);
    }

    /**
     * 리뷰 아이디로 상세 리뷰 조회 api
     *
     * @param reviewId 리뷰 아이디
     * @return reviewId에 해당 하는 리뷰 반환
     */
    @GetMapping("/review/{reviewId}")
    public ResponseEntity<ResponseBodyFormatter> getReview(@PathVariable("reviewId") Long reviewId) {
        log.info(String.valueOf(reviewId));

        return DataResponseBodyFormatter.put(SuccessCode.SUCCESS, reviewService.getReview(reviewId));
    }

    /**
     * 해당 유저의 스킬 및 매너 평균 점수 반환 api
     *
     * @param userId 팀 아이디
     * @return 해당 유저의 스킬 및 매너 평균 점수 반환
     */
    @GetMapping("/subReviewScore/{userId}")
    public ResponseEntity<ResponseBodyFormatter> subReviewScore(@PathVariable("userId") Long userId) {
        log.info(String.valueOf(userId));

        return DataResponseBodyFormatter.put(SuccessCode.SUCCESS, reviewService.subAvgScore(userId));
    }

    /**
     * 해당 팀의 스킬 및 매너 평균 점수 반환 api
     *
     * @param teamId 팀 아이디
     * @return 해당 팀의 스킬 및 매너 평균 점수 반환
     */
    @GetMapping("/teamReviewScore/{teamId}")
    public ResponseEntity<ResponseBodyFormatter> teamReviewScore(@PathVariable("teamId") Long teamId) {
        log.info(String.valueOf(teamId));

        return DataResponseBodyFormatter.put(SuccessCode.SUCCESS, reviewService.teamAvgScore(teamId));
    }
}
