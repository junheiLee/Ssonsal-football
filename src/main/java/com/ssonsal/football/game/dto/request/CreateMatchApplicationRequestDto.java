package com.ssonsal.football.game.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@NoArgsConstructor
public class CreateMatchApplicationRequestDto {

    private String uniform;
    private int subCount;

    public CreateMatchApplicationRequestDto(CreateGameRequestDto gameDto) {
        this.uniform = gameDto.getUniform();
        this.subCount = gameDto.getSubCount();
    }
}