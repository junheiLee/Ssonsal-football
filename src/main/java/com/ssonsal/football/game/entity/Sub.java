package com.ssonsal.football.game.entity;


import com.ssonsal.football.global.entity.BaseEntity;
import com.ssonsal.football.team.entity.Team;
import com.ssonsal.football.user.entity.User;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "sub", uniqueConstraints = {
        @UniqueConstraint(
                name = "uniqueUserAndGame",
                columnNames = {"user_id", "game_id"}
        )
})
public class Sub extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_id")
    private Game game;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private MatchApplication matchApplication;

    @Builder
    public Sub(MatchApplication matchApplication, User user, Game game) {
        this.matchApplication = matchApplication;
        this.user = user;
        this.game = game;
    }


}
