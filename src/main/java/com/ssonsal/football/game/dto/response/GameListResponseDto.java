package com.ssonsal.football.game.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import com.ssonsal.football.game.entity.Game;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@NoArgsConstructor
public class GameListResponseDto {

    private Long id;
    private String schedule;
    private String region;
    private String stadium;
    private Integer vsFormat;
    private String gender;
    private String rule;
    private Integer account;


    public GameListResponseDto(Game game) {

        this.id = game.getId();
        this.schedule = game.getSchedule().toString();
        this.region = game.getRegion();
        this.stadium = game.getStadium();
        this.vsFormat = game.getVsFormat();
        this.gender = game.getGender();
        this.rule = game.getRule();
        this.account = game.getAccount();
    }

    @QueryProjection
    public GameListResponseDto(Long id, String schedule, String region,
                               String stadium, Integer vsFormat, String gender,
                               String rule, Integer account) {
        this.id = id;
        this.schedule = schedule;
        this.region = region;
        this.stadium = stadium;
        this.vsFormat = vsFormat;
        this.gender = gender;
        this.rule = rule;
        this.account = account;
    }
}
