package com.ssonsal.football.admin.entity;

import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "game")
@NoArgsConstructor
public class Game extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id")
    private User writer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hometeam_id")
    private Team hometeam;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "awayteam_id")
    private Team awayteam;

    @NotNull
    private LocalDateTime schedule;

    @NotNull
    private int gameTime;

    @NotNull
    private String region;

    @NotNull
    private int vsFormat;

    private String stadium;
    private String gender;
    private String rule;
    private Integer account;
    private int matchStatus;
    private int deleteCode;
    private int hometeamResult;
    private int awayteamResult;

}
