package com.ssonsal.football.team.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TeamApplyDto {

    private Long id;

    private String nickname;

    private Integer age;

    private Integer gender;

    private String position;

    public TeamApplyDto(Long id, String nickname, Integer gender, String position,Integer age) {
        this.id = id;
        this.nickname = nickname;
        this.gender = gender;
        this.position = position;
        this.age = age;
    }

}
