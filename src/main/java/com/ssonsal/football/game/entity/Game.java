package com.ssonsal.football.game.entity;

import com.ssonsal.football.game.dto.request.GameRequestDto;
import com.ssonsal.football.global.entity.BaseEntity;
import com.ssonsal.football.team.entity.Team;
import com.ssonsal.football.user.entity.User;
import com.sun.istack.NotNull;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

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
    private List<MatchApplication> matchApplications;

    @Builder
    public Game(User writer, Team home,
                LocalDateTime schedule, MatchStatus matchStatus,
                GameRequestDto gameRequestDto) {
        this.writer = writer;
        this.home = home;
        this.schedule = schedule;
        this.matchStatus = matchStatus.getCodeNumber();
        this.gameTime = gameRequestDto.getGameTime();
        this.region = gameRequestDto.getRegion();
        this.stadium = gameRequestDto.getStadium();
        this.vsFormat = gameRequestDto.getVsFormat();
        this.gender = gameRequestDto.getGender();
        this.rule = gameRequestDto.getRule();
        this.account = gameRequestDto.getAccount();
    }

    public void approvalTeamApplicant(Team awayTeam) {
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
