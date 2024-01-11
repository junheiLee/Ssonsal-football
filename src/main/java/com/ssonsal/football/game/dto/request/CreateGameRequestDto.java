package com.ssonsal.football.game.dto.request;


import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.Future;
import javax.validation.constraints.Min;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;

@ToString
@Getter
public class CreateGameRequestDto {

    @Future(message = "날짜와 시간은 현재 이후이어야 합니다.")
    private LocalDateTime datetime;

    private int gameTime;
    private String region;
    private String stadium;

    @Min(value = 1, message = "인원제는 1 이상이어야 합니다.")
    private int vsFormat;
    private String gender;
    private String rule;

    @PositiveOrZero(message = "비용은 0 이상이어야 합니다.")
    private Integer account;
    private boolean findAway;
    private String uniform;
    @PositiveOrZero(message = "용병숫자는 0 이상이어야 합니다.")
    private int subCount;

}
