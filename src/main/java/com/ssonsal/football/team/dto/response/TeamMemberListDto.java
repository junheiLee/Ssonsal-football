package com.ssonsal.football.team.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TeamMemberListDto {

    private Long id;

    private String nickname;

    private Integer birth;

    private Integer gender;

    private String position;

    public TeamMemberListDto(Long id, String nickname, Integer gender, String position) {
        this.id = id;
        this.nickname = nickname;
        this.gender = gender;
        this.position = position;
    }
}
