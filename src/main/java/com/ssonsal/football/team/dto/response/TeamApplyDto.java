package com.ssonsal.football.team.dto.response;

import com.ssonsal.football.team.entity.TeamApply;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class TeamApplyDto {

    private Long id;

    private String nickname;

    private Integer age;

    private String gender;

    private String position;

    public TeamApplyDto(TeamApply teamApply, Integer age) {
        this.id = teamApply.getUser().getId();
        this.nickname = teamApply.getUser().getNickname();
        this.gender = teamApply.getUser().getGender();
        this.position = teamApply.getUser().getPosition();
        this.age = age;
    }

}
