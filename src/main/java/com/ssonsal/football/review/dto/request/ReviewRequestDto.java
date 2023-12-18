package com.ssonsal.football.review.dto.request;

import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewRequestDto {

    @NotNull
    private int reviewCode;

    @NotNull
    private Long writerId;

    @NotNull
    private Long targetId;

    @NotNull
    private Long gameId;

    private int skillScore;
    private int mannerScore;
    private String comment;
}


