package com.ssonsal.football.team.entity;

import com.ssonsal.football.user.entity.User;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Embeddable
@NoArgsConstructor
@EqualsAndHashCode
@Getter
public class RejectId implements Serializable {

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column
    private Long teamId;

    public RejectId(User user, Long teamId) {
        this.user = user;
        this.teamId = teamId;
    }
}
