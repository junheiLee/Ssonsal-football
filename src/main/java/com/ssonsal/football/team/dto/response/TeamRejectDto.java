package com.ssonsal.football.team.dto.response;

import com.ssonsal.football.team.entity.RejectId;
import com.ssonsal.football.team.entity.TeamReject;
import lombok.Getter;

@Getter
public class TeamRejectDto {

    private Long id;

    private String nickname;

    private Integer age;

    private String gender;

    private String position;

    public TeamRejectDto(TeamReject teamReject, Integer age) {
        RejectId rejectId = teamReject.getRejectId();
        this.id = rejectId.getUser().getId();
        this.nickname = rejectId.getUser().getNickname();
        this.gender = rejectId.getUser().getGender();
        this.position = rejectId.getUser().getPosition();
        this.age = age;
    }

}
