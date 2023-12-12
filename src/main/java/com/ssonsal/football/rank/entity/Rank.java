package com.ssonsal.football.rank.entity;

import com.ssonsal.football.global.entity.BaseEntity;
import com.ssonsal.football.team.entity.Team;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "ranks")
public class Rank extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    private int spot;

    @Builder
    public Rank(Team team, int spot) {
        this.team = team;
        this.spot = spot;
        team.getTeamRecord().enterRank(spot);
    }
}
