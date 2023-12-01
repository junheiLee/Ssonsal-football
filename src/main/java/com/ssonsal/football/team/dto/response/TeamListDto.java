package com.ssonsal.football.team.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TeamListDto {

    private Long id;

    private String name;

    private String preferredArea;

    private Integer ranking;

    private Float skillScore;

    private Float ageAverage;

    public TeamListDto(long id, String name, String preferredArea, float skillScore) {
        this.id = id;
        this.name = name;
        this.preferredArea = preferredArea;
        this.skillScore = skillScore;
    }
    
}