package com.ssonsal.football.rank.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@NoArgsConstructor
public class MonthRequest {
    private Integer month;

    public MonthRequest(Integer month) {
        this.month = month;
    }

}