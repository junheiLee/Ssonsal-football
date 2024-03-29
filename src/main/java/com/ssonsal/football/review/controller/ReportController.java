package com.ssonsal.football.review.controller;

import com.ssonsal.football.admin.exception.AdminErrorCode;
import com.ssonsal.football.admin.service.UserManagementService;
import com.ssonsal.football.global.account.Account;
import com.ssonsal.football.global.account.CurrentUser;
import com.ssonsal.football.global.exception.CustomException;
import com.ssonsal.football.global.util.SuccessCode;
import com.ssonsal.football.global.util.formatter.DataResponseBodyFormatter;
import com.ssonsal.football.global.util.formatter.ResponseBodyFormatter;
import com.ssonsal.football.review.dto.request.ReportRequestDto;
import com.ssonsal.football.review.service.ReportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportService reportService;
    private final UserManagementService userManagementService;

    /**
     * 신고글 목록 조회 api
     *
     * @return 신고 글 list 반환
     */
    @GetMapping
    public ResponseEntity<ResponseBodyFormatter> getAllReports(@CurrentUser Account account) {

        Long user = account.getId();

        if (userManagementService.isAdmin(user)) {
            throw new CustomException(AdminErrorCode.ADMIN_AUTH_FAILED);
        }

        return DataResponseBodyFormatter.put(SuccessCode.SUCCESS, reportService.getAllReports());
    }

    /**
     * 신고글 생성 시, 호출 되는 api
     *
     * @param reportRequestDto 산고 생성에 필요한 정보
     * @return 성공 코드와 생성된 신고를 ResponseBody 에 담아 반환
     */
    @PostMapping
    public ResponseEntity<ResponseBodyFormatter> createReport(@RequestBody ReportRequestDto reportRequestDto, @CurrentUser Account account) {

        Long user = account.getId();

        return DataResponseBodyFormatter.put(SuccessCode.SUCCESS, reportService.createReport(reportRequestDto, user));
    }

    /**
     * 신고글 삭제 코드를 변경 하는 api
     *
     * @param reportId 신고 아이디
     * @return 성공 메시지 반환
     */
    @PatchMapping("/{reportId}")
    public ResponseEntity<ResponseBodyFormatter> updateReportCode(
            @PathVariable Long reportId, @RequestBody Long reviewId, @CurrentUser Account account) {

        Long user = account.getId();

        if (userManagementService.isAdmin(user)) {
            throw new CustomException(AdminErrorCode.ADMIN_AUTH_FAILED);
        }

        reportService.updateDeleteCode(reportId, reviewId);

        return DataResponseBodyFormatter.put(SuccessCode.SUCCESS);
    }
}
