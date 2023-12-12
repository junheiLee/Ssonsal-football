package com.ssonsal.football.game.entity;

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
    @JoinColumn(name = "match_application_id")
    private MatchApplication matchApplication;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id") // user
    private User user;

    private String subApplicantStatus; // default 대기;

    @Builder
    public SubApplicant(MatchApplication matchApplication, User user, String subApplicantStatus) {
        this.matchApplication = matchApplication;
        this.user = user;
        this.subApplicantStatus = subApplicantStatus;
    }

    public void reject() {
        this.subApplicantStatus = ApplicantStatus.REFUSAL.getDescription();
    }
}
