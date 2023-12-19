package com.ssonsal.football.game.entity;

import com.ssonsal.football.game.dto.request.CreateMatchApplicationRequestDto;
import com.ssonsal.football.global.entity.BaseEntity;
import com.ssonsal.football.team.entity.Team;
import com.ssonsal.football.user.entity.User;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static com.ssonsal.football.game.entity.ApplicantStatus.*;

@Entity
@Getter
@ToString(exclude = {"team", "game", "applicant", "subApplicants"})
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

    @OneToMany(mappedBy = "matchApplication")
    private final List<SubApplicant> subApplicants = new ArrayList<>();

    @Builder
    public MatchApplication(User applicant, Team team, Game game,
                            String applicationStatus, CreateMatchApplicationRequestDto matchTeamDto) {
        this.applicant = applicant;
        this.team = team;
        this.game = game;
        this.applicationStatus = applicationStatus;
        this.uniform = matchTeamDto.getUniform();
        this.subCount = matchTeamDto.getSubCount();
        game.getMatchApplications().add(this);
    }

    public MatchApplication update(CreateMatchApplicationRequestDto updateHomeTeamDto) {

        this.uniform = updateHomeTeamDto.getUniform();
        this.subCount = updateHomeTeamDto.getSubCount();

        return this;
    }

    public void approve() {
        this.applicationStatus = APPROVAL.getDescription();
        game.approveTeamApplicant(this.applicant, this.team);
    }

    public void reject() {
        this.applicationStatus = REFUSAL.getDescription();
    }

    public void changeStatusToSuspension() {
        this.applicationStatus = SUSPENSION.getDescription();
    }

    public void closeSub() {
        this.subCount = 0;
    }

    public void acceptSub() {
        this.subCount -= 1;
    }
}
