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
    private String review;
    private String reason;
    private int reportCode;
    private int reviewDeleteCode;
    private Long reviewId;
    private LocalDate createdAt;
    private LocalDate modifiedAt;

    public static ReportResponseDto fromEntity(Report report) {
        return new ReportResponseDtoBuilder()
                .id(report.getId())
                .review(report.getReview().getComment())
                .reason(report.getReason())
                .reportCode(report.getReportCode())
                .reviewDeleteCode(report.getReview().getDeleteCode())
                .reviewId(report.getReview().getId())
                .createdAt(LocalDate.from(report.getCreatedAt()))
                .modifiedAt(LocalDate.from((report.getModifiedAt())))
                .build();
    }
}