package com.ssonsal.football.game.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class MatchTeamResponseDto {

    private Long teamId;
    private String logoUrl;
    private String name;
    private Float skillScore;
    private String uniform;
    private int subCount;
    private boolean hasSub;

    @QueryProjection
    public MatchTeamResponseDto(Long teamId, String logoUrl, String name, Float skillScore, String uniform, int subCount) {
        this.teamId = teamId;
        this.logoUrl = logoUrl;
        this.name = name;
        this.skillScore = skillScore;
        this.uniform = uniform;
        this.subCount = subCount;
    }

    public void isHavingSub(boolean hasSub) {
        this.hasSub = hasSub;
    }
}
