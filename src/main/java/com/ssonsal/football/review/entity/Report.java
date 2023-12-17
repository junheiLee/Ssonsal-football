package com.ssonsal.football.review.entity;

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
@Table(name = "report")
public class Report extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id")
    @NotNull
    private Review review;

    private String reason;
    private int reportCode;

    @ManyToOne
    @JoinColumn(name = "writer_id")
    private User user;

    @Builder
    public Report(Review review, String reason, User user) {
        this.user = user;
        this.review = review;
        this.reason = reason;
        this.reportCode = reportCode;
    }
}

