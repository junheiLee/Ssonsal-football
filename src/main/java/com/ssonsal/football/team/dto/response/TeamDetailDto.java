package com.ssonsal.football.team.dto.response;

import com.ssonsal.football.team.entity.Team;
import com.ssonsal.football.team.entity.TeamRecord;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TeamDetailDto {

    private Long id;

    private String name;

    private String logoUrl;

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

    private Integer memberCount;

    @Builder
    public TeamDetailDto(Team team, TeamRecord teamRecord, Integer memberCount, String leaderName) {
        this.id = team.getId();
        this.logoUrl = team.getLogoUrl();
        this.name = team.getName();
        this.preferredArea = team.getPreferredArea();
        this.preferredTime = team.getPreferredTime();
        this.intro = team.getIntro();
        this.winCount = teamRecord.getWinCount();
        this.drawCount = teamRecord.getDrawCount();
        this.loseCount = teamRecord.getLoseCount();
        this.mannerScore = team.getMannerScore();
        this.skillScore = team.getSkillScore();
        this.memberCount = memberCount;
        this.ranking = teamRecord.getRank();
        this.leaderName = leaderName;
    }

}
