package com.ssonsal.football.game.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@NoArgsConstructor
public class GameResultResponseDto {

    private String homeResult;
    private String awayResult;
    private Integer totalScore;

    @Builder
    public GameResultResponseDto(String homeResult, String awayResult) {
        this.homeResult = homeResult;
        this.awayResult = awayResult;
    }

    public GameResultResponseDto setTotalScore(Integer totalScore) {
        this.totalScore = totalScore;
        return this;
    }
}
