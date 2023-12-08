package com.ssonsal.football.team.entity;

import com.ssonsal.football.global.entity.BaseEntity;
import lombok.AccessLevel;
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

    private int rank;

    public TeamRecord(Team team) {
        this.team = team;
    }
}
