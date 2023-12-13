package com.ssonsal.football.review.dto.response;

import com.ssonsal.football.review.etity.Report;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class ReportResponseDto {
    private Long id;
    private Long reviewId;
    private String reason;
    private int reportCode;
    private LocalDate createdAt;
    private LocalDate modifiedAt;

    public static ReportResponseDto fromEntity(Report report) {
        return new ReportResponseDtoBuilder()
                .id(report.getId())
                .reviewId(report.getReview().getId())
                .reason(report.getReason())
                .reportCode(report.getReportCode())
                .createdAt(LocalDate.from(report.getCreatedAt()))
                .modifiedAt(LocalDate.from((report.getModifiedAt())))
                .build();
    }
}