package com.ssonsal.football.review.entity;

import com.ssonsal.football.global.entity.BaseEntity;
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

    @Builder
    public Report(Long id, Review review, String reason, int reportCode) {
        this.id = id;
        this.review = review;
        this.reason = reason;
        this.reportCode = reportCode;
    }
}

