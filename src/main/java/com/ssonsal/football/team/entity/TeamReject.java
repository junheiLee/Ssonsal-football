package com.ssonsal.football.team.entity;


import com.ssonsal.football.global.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "team_reject")
public class TeamReject extends BaseEntity {

    @EmbeddedId
    private RejectId rejectId;

}
