package com.ssonsal.football.game.entity;

import com.ssonsal.football.game.dto.request.CreateGameRequestDto;
import com.ssonsal.football.global.entity.BaseEntity;
import com.ssonsal.football.team.entity.Team;
import com.ssonsal.football.user.entity.User;
import com.sun.istack.NotNull;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.ssonsal.football.game.entity.ApplicantStatus.WAITING;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(exclude = {"writer", "home", "away"})
public class Game extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id")
    private User writer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hometeam_id")
    private Team home;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "away_applicant_id")
    private User awayApplicant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "awayteam_id")
    private Team away;

    private int matchStatus;

    @NotNull
    private LocalDateTime schedule;

    @NotNull
    private int gameTime;

    private String region;
    private String stadium;
    private int vsFormat;
    private String gender;
    private String rule;
    private Integer account;
    private int deleteCode;
    private String hometeamResult;
    private String awayteamResult;

    @OneToMany(mappedBy = "game")
    private List<MatchApplication> matchApplications = new ArrayList<>();

    @Builder
    public Game(User writer, Team home,
                LocalDateTime schedule, MatchStatus matchStatus,
                CreateGameRequestDto createGameRequestDto) {
        this.writer = writer;
        this.home = home;
        this.schedule = schedule;
        this.matchStatus = matchStatus.getCodeNumber();
        this.gameTime = createGameRequestDto.getGameTime();
        this.region = createGameRequestDto.getRegion();
        this.stadium = createGameRequestDto.getStadium();
        this.vsFormat = createGameRequestDto.getVsFormat();
        this.gender = createGameRequestDto.getGender();
        this.rule = createGameRequestDto.getRule();
        this.account = createGameRequestDto.getAccount();
    }

    public Game update(LocalDateTime schedule, CreateGameRequestDto createGameRequestDto) {

        this.schedule = schedule;
        this.gameTime = createGameRequestDto.getGameTime();
        this.region = createGameRequestDto.getRegion();
        this.stadium = createGameRequestDto.getStadium();
        this.vsFormat = createGameRequestDto.getVsFormat();
        this.gender = createGameRequestDto.getGender();
        this.rule = createGameRequestDto.getRule();
        this.account = createGameRequestDto.getAccount();

        return this;
    }

    public void changeRemainApplicationsStatus() {
        matchApplications.stream()
                .filter(application -> application.getApplicationStatus().equals(WAITING.getDescription()))
                .forEach(MatchApplication::changeStatusToSuspension);
    }

    public void approveTeamApplicant(User applicant, Team awayTeam) {
        this.awayApplicant = applicant;
        this.away = awayTeam;
        this.matchStatus = MatchStatus.CONFIRMED.getCodeNumber();
    }

    public void enterHomeTeamResult(String homeResult) {
        this.hometeamResult = homeResult;
    }

    public void enterAwayTeamResult(String awayResult) {
        this.awayteamResult = awayResult;
    }

    public void end() {
        this.matchStatus = MatchStatus.END.getCodeNumber();
    }
}
