package com.ssonsal.football.review.dto.response;

import com.ssonsal.football.review.etity.Report;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReportResponseDto {
    private Long id;
    private Long reviewId;
    private String reason;
    private int reportCode;

    public static ReportResponseDto fromEntity(Report report) {
        ReportResponseDto reportResponseDto = new ReportResponseDto();
        reportResponseDto.setId(report.getId());
        reportResponseDto.setReviewId(report.getReview().getId());
        reportResponseDto.setReason(report.getReason());
        reportResponseDto.setReportCode(report.getReportCode());
        return reportResponseDto;
    }
}

