package com.ssonsal.football.team.dto.response;

import com.ssonsal.football.team.entity.Team;
import lombok.Getter;

@Getter
public class TeamEditFormDto {

    private String logoUrl;

    private String name;

    private String intro;

    private Integer recruit;

    public TeamEditFormDto(Team team) {
        this.logoUrl = team.getLogoUrl();
        this.name = team.getName();
        this.intro = team.getIntro();
        this.recruit = team.getRecruit();
    }

}
