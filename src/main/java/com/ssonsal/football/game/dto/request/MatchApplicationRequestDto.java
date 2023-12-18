package com.ssonsal.football.game.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@NoArgsConstructor
public class MatchApplicationRequestDto {

    private String uniform;
    private int subCount;

    public MatchApplicationRequestDto(GameRequestDto gameDto) {
        this.uniform = gameDto.getUniform();
        this.subCount = gameDto.getSubCount();
    }
}