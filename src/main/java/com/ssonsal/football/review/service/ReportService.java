package com.ssonsal.football.review.service;

import com.ssonsal.football.review.dto.request.ReportRequestDto;
import com.ssonsal.football.review.dto.response.ReportResponseDto;

import java.util.List;

public interface ReportService {
    List<ReportResponseDto> getAllReports();

    ReportResponseDto createReport(ReportRequestDto reportRequestDto, Long userId);

    void updateDeleteCode(Long reportId, Integer reportCode);
}
