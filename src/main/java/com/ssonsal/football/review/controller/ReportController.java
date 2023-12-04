package com.ssonsal.football.review.controller;

import com.ssonsal.football.global.util.SuccessCode;
import com.ssonsal.football.global.util.formatter.DataResponseBodyFormatter;
import com.ssonsal.football.global.util.formatter.ResponseBodyFormatter;
import com.ssonsal.football.review.dto.request.ReportRequestDto;
import com.ssonsal.football.review.dto.response.ReportResponseDto;
import com.ssonsal.football.review.service.ReportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/reports")
public class ReportController {

    private final ReportService reportService;

    @GetMapping
    public ResponseEntity<ResponseBodyFormatter> getAllReports() {
        List<ReportResponseDto> reports = reportService.getAllReports();
        return DataResponseBodyFormatter.put(SuccessCode.SUCCESS, reports);
    }

    @PostMapping
    public ResponseEntity<ResponseBodyFormatter> createReport(
            @RequestBody ReportRequestDto reportRequestDto) {
        ReportResponseDto createdReport = reportService.createReport(reportRequestDto);
        return DataResponseBodyFormatter.put(SuccessCode.SUCCESS, createdReport);
    }
}
