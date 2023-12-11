package com.ssonsal.football.game.dto.response;

import com.ssonsal.football.game.entity.Game;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.ssonsal.football.game.util.GameConstant.SCHEDULE_FORMAT;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GameDetailResponseDto {

    private int matchStatus;
    private String schedule;
    private int gameTime;
    private String region;
    private String stadium;
    private int vsFormat;
    private String gender;
    private String rule;
    private Integer account;
    private String homeResult;
    private String awayResult;

    public GameDetailResponseDto(Game game) {
        this.matchStatus = game.getMatchStatus();
        this.schedule = toStringSchedule(game.getSchedule());
        this.gameTime = game.getGameTime();
        this.region = game.getRegion();
        this.stadium = game.getStadium();
        this.vsFormat = game.getVsFormat();
        this.gender = game.getGender();
        this.rule = game.getRule();
        this.account = game.getAccount();
        this.homeResult = game.getHometeamResult();
        this.awayResult = game.getAwayteamResult();
    }

    private String toStringSchedule(LocalDateTime schedule) {
        return schedule.format(DateTimeFormatter.ofPattern(SCHEDULE_FORMAT));
    }

}
