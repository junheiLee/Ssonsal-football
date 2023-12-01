package com.ssonsal.football.team.entity;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@NoArgsConstructor
@EqualsAndHashCode
public class RejectId implements Serializable {

    @Column
    private Long userId;

    @Column
    private Long teamId;


}
