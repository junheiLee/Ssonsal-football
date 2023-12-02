package com.ssonsal.football.review.etity;

import com.ssonsal.football.game.entity.Game;
import com.ssonsal.football.global.entity.BaseEntity;
import com.ssonsal.football.user.entity.User;
import com.sun.istack.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "review")
public class Review extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private int reviewCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id")
    @NotNull
    private User user;

    @NotNull
    private Long targetId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_id")
    @NotNull
    private Game game;

    private int skillScore;
    private int mannerScore;
    private String comment;
    private int deleteCode;

    @Builder
    public Review(User user, Game game, int reviewCode, Long targetId, int mannerScore, int skillScore, String comment){
        this.user = user;
        this.game = game;
        this.reviewCode = reviewCode;
        this.targetId = targetId;
        this.mannerScore = mannerScore;
        this.skillScore = skillScore;
        this.comment = comment;
    }
}
