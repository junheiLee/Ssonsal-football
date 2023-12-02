package com.ssonsal.football.team.entity;

import com.ssonsal.football.global.entity.BaseEntity;
import com.ssonsal.football.user.entity.User;
import com.sun.istack.NotNull;
import lombok.*;

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
    private String logo;

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
    @Setter
    private Long leaderId;

    @OneToMany(mappedBy = "team")
    List<User> users = new ArrayList<>();

    @Builder
    public Team(String name, String logo, String preferredArea, String preferredTime, Integer recruit, String intro, Float mannerScore, Float skillScore, Long leaderId) {
        this.name = name;
        this.logo = logo;
        this.preferredArea = preferredArea;
        this.preferredTime = preferredTime;
        this.recruit = recruit;
        this.intro = intro;
        this.mannerScore = mannerScore;
        this.skillScore = skillScore;
        this.leaderId = leaderId;
    }
}
