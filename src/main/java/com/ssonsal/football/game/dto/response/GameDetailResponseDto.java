package com.ssonsal.football.game.dto.response;

import com.ssonsal.football.game.entity.Game;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import static com.ssonsal.football.game.util.Transfer.toStringSchedule;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GameDetailResponseDto {

    private Long homeId;
    private Long awayId;
    private Long homeApplicationId;
    private Long awayApplicationId;
    private Long writerId;
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

    public GameDetailResponseDto(Game game, Long homeApplicationId, Long awayId, Long awayApplicationId) {
        this.homeId = game.getHome().getId();
        this.awayId = awayId;
        this.homeApplicationId = homeApplicationId;
        this.awayApplicationId = awayApplicationId;
        this.writerId = game.getWriter().getId();
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


}
