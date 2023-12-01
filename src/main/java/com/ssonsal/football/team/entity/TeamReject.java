package com.ssonsal.football.team.entity;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "team_reject")
public class TeamReject {

    @EmbeddedId
    private RejectId rejectId;

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

    public TeamReject(RejectId rejectId) {
        this.rejectId = rejectId;
    }

}