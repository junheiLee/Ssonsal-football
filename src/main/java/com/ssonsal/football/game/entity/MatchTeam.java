package com.ssonsal.football.game.entity;

import com.ssonsal.football.game.dto.request.MatchApplicantRequestDto;
import com.ssonsal.football.global.entity.BaseEntity;
import com.ssonsal.football.team.entity.Team;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@ToString(exclude = {"team", "game"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "match_team",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "unique_team_and_game",
                        columnNames = {"team_id", "game_id"}
                )
        })
public class MatchTeam extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_id", nullable = false)
    private Game game;

    private String uniform;
    private int subCount;

    private String matchApplicantStatus;

    @Builder
    public MatchTeam(Team team, Game game, String matchApplicantStatus, MatchApplicantRequestDto matchTeamDto) {
        this.team = team;
        this.game = game;
        this.matchApplicantStatus = matchApplicantStatus;
        this.uniform = matchTeamDto.getUniform();
        this.subCount = matchTeamDto.getSubCount();
    }

}
