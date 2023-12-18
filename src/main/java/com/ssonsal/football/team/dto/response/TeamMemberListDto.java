package com.ssonsal.football.team.dto.response;


import com.ssonsal.football.user.entity.User;
import lombok.Getter;

@Getter
public class TeamMemberListDto {

    private Long id;

    private String nickname;

    private Integer age;

    private String gender;

    private String position;

    public TeamMemberListDto(User user, Integer age) {
        this.id = user.getId();
        this.nickname = user.getNickname();
        this.gender = user.getGender();
        this.position = user.getPosition();
        this.age = age;
    }
}
