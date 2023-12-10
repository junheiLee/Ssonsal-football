package com.ssonsal.football.game.dto.request;

import com.ssonsal.football.game.entity.Game;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class SubRecordDto {

    private LocalDateTime schedule;
    private String gameRegion;
    private String stadium;
    private Integer vsFormat;
    private String gameRule;


    public SubRecordDto(LocalDateTime schedule,String gameRegion,String stadium,Integer vsFormat,String gameRule) {
        this.schedule = schedule;
        this.gameRegion = gameRegion;
        this.stadium = stadium;
        this.vsFormat = vsFormat;
        this.gameRule = gameRule;
    }

}
