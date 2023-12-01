package com.ssonsal.football.team.entity;

import com.ssonsal.football.global.entity.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "team_record")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
public class TeamRecord extends BaseEntity {

    @Id
    private Long id;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    @ColumnDefault("0")
    private Integer point;

    @ColumnDefault("0")
    private Integer winCount;

    @ColumnDefault("0")
    private Integer drawCount;

    @ColumnDefault("0")
    private Integer loseCount;

    @ColumnDefault("0")
    private Integer totalGameCount;

    @ColumnDefault("-1")
    private Integer rank;

    public TeamRecord(Team team) {
        this.team = team;
    }
}
