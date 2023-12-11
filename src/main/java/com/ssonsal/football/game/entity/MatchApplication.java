package com.ssonsal.football.game.entity;

import com.ssonsal.football.game.dto.request.MatchApplicationRequestDto;
import com.ssonsal.football.global.entity.BaseEntity;
import com.ssonsal.football.team.entity.Team;
import com.ssonsal.football.user.entity.User;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@ToString(exclude = {"team", "game"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "match_application",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "unique_team_and_game",
                        columnNames = {"team_id", "game_id"}
                )
        })
public class MatchApplication extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "applicant_id", nullable = false)
    private User applicant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_id", nullable = false)
    private Game game;

    private String uniform;
    private int subCount;

    private String applicationStatus;

    @Builder
    public MatchApplication(User applicant, Team team, Game game,
                            String applicationStatus, MatchApplicationRequestDto matchTeamDto) {
        this.applicant = applicant;
        this.team = team;
        this.game = game;
        this.applicationStatus = applicationStatus;
        this.uniform = matchTeamDto.getUniform();
        this.subCount = matchTeamDto.getSubCount();
        // game.getMatchTeams().add(this);
    }

    public MatchApplication update(MatchApplicationRequestDto updateHomeTeamDto) {

        this.uniform = updateHomeTeamDto.getUniform();
        this.subCount = updateHomeTeamDto.getSubCount();

        return this;
    }

    public void approve() {
        this.applicationStatus = ApplicantStatus.APPROVAL.getDescription();
        game.approveTeamApplicant(this.applicant, this.team);
    }

    public void reject() {
        this.applicationStatus = ApplicantStatus.REFUSAL.getDescription();
    }

    public void changeStatusToWaiting() {
        this.applicationStatus = ApplicantStatus.WAITING.getDescription();
    }

    public void decreaseSubCount() {
        this.subCount -= 1;
    }
}