package com.ssonsal.football.team.entity;

import com.ssonsal.football.game.util.TeamResult;
import com.ssonsal.football.global.entity.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "team_record")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TeamRecord extends BaseEntity {

    @Id
    private Long id;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    private int point;

    private int winCount;

    private int drawCount;

    private int loseCount;

    private int totalGameCount;

    @Column(name = "team_rank")
    private int rank;


    /**
     * 생성할때 rank 기본값을 -1로 생성
     *
     * @param team
     */
    public TeamRecord(Team team) {
        this.team = team;
        this.rank = -1;
    }

    @Builder
    public TeamRecord(int winCount, int drawCount, int loseCount) {
        this.winCount = winCount;
        this.drawCount = drawCount;
        this.loseCount = loseCount;
    }

    public TeamRecord update(TeamRecord updateRecord) {
        this.winCount += updateRecord.winCount;
        this.drawCount += updateRecord.drawCount;
        this.loseCount += updateRecord.loseCount;
        this.totalGameCount++;
        this.point = (this.winCount * (TeamResult.WIN.getScore() + 1))
                + (this.drawCount * TeamResult.DRAW.getScore())
                + (this.loseCount * TeamResult.LOSE.getScore());

        return this;
    }

    public void enterRank(int rank) {
        this.rank = rank;
    }

//    public TeamRecord enterTeamToRecordSet(Team team){
//        this.team = team;
//        return this;
//    }
}
