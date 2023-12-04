package com.ssonsal.football.review.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReportRequestDto {
    private Long reviewId;
    private String reason;
    private int reportCode;
}
