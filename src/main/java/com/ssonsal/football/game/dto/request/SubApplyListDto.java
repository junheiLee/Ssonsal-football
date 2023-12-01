package com.ssonsal.football.game.dto.request;

import com.ssonsal.football.team.entity.Team;
import com.ssonsal.football.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SubApplyListDto {

    private User user; // user_id

    private Team team; // team_id

    private int subApplicantStatus; // default 0; 0: 대기, 1: 확정, 2: 거절
}
