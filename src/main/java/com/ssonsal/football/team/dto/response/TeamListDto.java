package com.ssonsal.football.team.dto.response;

import com.ssonsal.football.team.entity.Team;
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

    private String ageAverage;

    public TeamListDto(Team team, Integer ranking, String ageAverage) {
        this.id = team.getId();
        this.name = team.getName();
        this.preferredArea = team.getPreferredArea();
        this.skillScore = team.getSkillScore();
        this.ranking = ranking;
        this.ageAverage = ageAverage;
    }


}
