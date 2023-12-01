package com.ssonsal.football.team.entity;

import com.ssonsal.football.user.entity.User;
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
@Table(name = "team_apply")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TeamApply {

    @Id
    private Long id;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

    public TeamApply(User user, Team team) {
        this.user = user;
        this.team = team;
    }


}
