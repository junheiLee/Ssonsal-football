package com.ssonsal.football.game.entity;

import com.ssonsal.football.global.entity.BaseEntity;
import com.ssonsal.football.user.entity.User;
import com.sun.istack.NotNull;

import javax.persistence.*;

@Entity
@Table(name = "sub_applicant", uniqueConstraints = {
        @UniqueConstraint(
                name = "unique_user_and_match_application",
                columnNames = {"user_id", "match_application_id"}
        )
})
public class SubApplicant extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "match_application_id") // match_team
    private MatchApplication matchApplication;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id") // user
    private User user;

    private int subApplicantStatus; // default 0; 0: 대기, 1: 확정, 2: 거절

}
