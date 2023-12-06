package com.ssonsal.football.game.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@NoArgsConstructor
public class GameResultResponseDto {

    private Integer homeTeamResult;
    private Integer awayTeamResult;
    private Integer totalResult;

    @Builder
    public GameResultResponseDto(Integer homeTeamResult, Integer awayTeamResult, Integer totalResult) {
        this.homeTeamResult = homeTeamResult;
        this.awayTeamResult = awayTeamResult;
        this.totalResult = totalResult;
    }
}
