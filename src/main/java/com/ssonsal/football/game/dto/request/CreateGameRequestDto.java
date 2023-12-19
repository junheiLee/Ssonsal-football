package com.ssonsal.football.game.dto.request;


import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class CreateGameRequestDto {

    private String datetime;
    private int gameTime;
    private String region;
    private String stadium;
    private int vsFormat;
    private String gender;
    private String rule;
    private Integer account;
    private boolean findAway;
    private String uniform;
    private int subCount;

}
