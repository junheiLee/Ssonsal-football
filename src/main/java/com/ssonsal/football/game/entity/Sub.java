package com.ssonsal.football.game.entity;


import com.ssonsal.football.global.entity.BaseEntity;
import com.ssonsal.football.user.entity.User;

import javax.persistence.*;

@Entity
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

}
