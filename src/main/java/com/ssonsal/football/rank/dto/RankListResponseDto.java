package com.ssonsal.football.rank.dto;

import com.ssonsal.football.team.entity.Team;
import com.ssonsal.football.team.entity.TeamRecord;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@NoArgsConstructor
public class RankListResponseDto {

    private Long id;

    private String name;

    private String logoUrl;

    private int totalGameCount;

    private int winCount;

    private int drawCount;

    private int loseCount;

    private int point;


    public RankListResponseDto(Team team, TeamRecord teamRecord) {
        this.id = team.getId();
        this.name = team.getName();
        this.logoUrl = team.getLogoUrl();
        this.totalGameCount = teamRecord.getTotalGameCount();
        this.winCount = teamRecord.getWinCount();
        this.drawCount = teamRecord.getDrawCount();
        this.loseCount = teamRecord.getLoseCount();
        this.point = teamRecord.getPoint();

    }

    public void resetRank() {
        this.totalGameCount = 0;
        this.winCount = 0;
        this.drawCount = 0;
        this.loseCount = 0;
        this.point = 0;
    }
}
