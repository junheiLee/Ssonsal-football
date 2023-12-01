package com.ssonsal.football.admin.entity;

import com.sun.istack.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name;

    @Column(name = "team_id")
    private Long teamId;

    @Column(columnDefinition = "integer default '0'" )
    private Integer role;

    @NotNull
    @Column(unique = true)
    private String email;

    @NotNull
    private String gender;

    @NotNull
    private String password;

    @NotNull
    private LocalDateTime birth;

    @NotNull
    @Column(unique = true)
    private String nickname;

    @Column(length = 4, columnDefinition = "CHAR(4) DEFAULT '올라운더'")
    private String position;

    @NotNull
    private String phone;

    @Column(name = "preferred_time", length = 20, nullable = true, columnDefinition = "VARCHAR(20) DEFAULT 'Anytime'")
    private String preferredTime;

    @Column(name = "preferred_area", length = 30, nullable = true, columnDefinition = "VARCHAR(30) DEFAULT 'Anywhere'")
    private String preferredArea;

    @Column(name = "intro", columnDefinition = "TEXT")
    private String intro;

    @Column(name = "skill_score")
    private Float skillScore;

    @Column(name = "manner_score")
    private Float mannerScore;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "modified_at")
    private LocalDateTime modifiedAt;



}