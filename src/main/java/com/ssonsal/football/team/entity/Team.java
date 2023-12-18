package com.ssonsal.football.team.entity;

import com.ssonsal.football.global.entity.BaseEntity;
import com.ssonsal.football.team.dto.request.TeamCreateDto;
import com.ssonsal.football.team.dto.request.TeamEditDto;
import com.ssonsal.football.user.entity.User;
import com.sun.istack.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@Table(name = "team")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Team extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    @NotBlank
    private String name;

    @Column(length = 1000)
    private String logoUrl;

    @Column(length = 1000)
    private String logoKey;

    private String preferredArea;

    private String preferredTime;

    private Integer recruit;

    @Column(length = 1000)
    private String intro;

    private Float mannerScore;

    private Float skillScore;

    @OneToOne(mappedBy = "team")
    private TeamRecord teamRecord;

    @Column(unique = true)
    @NotNull
    private Long leaderId;

    @OneToMany(mappedBy = "team")
    List<User> users = new ArrayList<>();

    @Builder
    public Team(TeamCreateDto teamCreateDto, String logoUrl, String logoKey) {
        this.name = teamCreateDto.getName();
        this.logoUrl = logoUrl;
        this.logoKey = logoKey;
        this.preferredArea = teamCreateDto.getPreferredArea();
        this.preferredTime = teamCreateDto.getPreferredTime();
        this.recruit = teamCreateDto.getRecruit();
        this.intro = teamCreateDto.getIntro();
        this.mannerScore = teamCreateDto.getMannerScore();
        this.skillScore = teamCreateDto.getSkillScore();
        this.leaderId = teamCreateDto.getLeaderId();
    }

    /**
     * 팀 정보를 업데이트 한다
     *
     * @param teamEditDto 팀 정보 DTO
     * @param logoUrl     바뀐 로고 URL
     * @param logoKey     바뀐 로고 키
     */
    public void TeamUpdate(TeamEditDto teamEditDto, String logoUrl, String logoKey) {
        this.name = teamEditDto.getName();
        this.logoUrl = logoUrl;
        this.logoKey = logoKey;
        this.preferredArea = teamEditDto.getPreferredArea();
        this.preferredTime = teamEditDto.getPreferredTime();
        this.recruit = teamEditDto.getRecruit();
        this.intro = teamEditDto.getIntro();
    }

    /**
     * 리더를 지정한다.
     *
     * @param userId 유저 아이디
     */
    public void delegateLeader(Long userId) {
        this.leaderId = userId;
    }

    public void updateScore(Float mannerScore, Float skillScore) {
        this.mannerScore = mannerScore;
        this.skillScore = skillScore;
    }
}
