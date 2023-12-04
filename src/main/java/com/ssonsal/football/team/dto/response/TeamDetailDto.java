package com.ssonsal.football.team.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TeamDetailDto {

    private Long id;

    private String name;

    private String preferredArea;

    private String preferredTime;

    private String intro;

    private Float mannerScore;

    private Float skillScore;

    private String leaderName;

    private Integer ranking;

    private Integer winCount;

    private Integer drawCount;

    private Integer loseCount;

    private Long memberCount;

    public TeamDetailDto(Long id, String name, String preferredArea, String preferredTime, String intro,
                         Integer winCount, Integer drawCount, Integer loseCount,
                         Float mannerScore, Float skillScore, Long memberCount) {
        this.id = id;
        this.name = name;
        this.preferredArea = preferredArea;
        this.preferredTime = preferredTime;
        this.intro = intro;
        this.winCount = winCount;
        this.drawCount = drawCount;
        this.loseCount = loseCount;
        this.mannerScore = mannerScore;
        this.skillScore = skillScore;
        this.memberCount = memberCount;
    }

}