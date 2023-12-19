package com.ssonsal.football.review.dto.response;

import com.ssonsal.football.review.entity.Report;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class ReportResponseDto {
    private Long id;
    private String reason;
    private Long reviewId;
    private LocalDate createdAt;
    private String writerNickname;

    public ReportResponseDto(Report report) {
        this.id = report.getId();
        this.reason = report.getReason();
        this.reviewId = report.getReview().getId();
        this.writerNickname = report.getUser().getNickname();
        this.createdAt = report.getCreatedAt().toLocalDate();
    }

}