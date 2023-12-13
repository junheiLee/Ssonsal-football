package com.ssonsal.football.game.dto.response;

import com.ssonsal.football.game.entity.SubApplicant;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@NoArgsConstructor
@ToString
public class SubApplicantsResponseDto {

    private Long applicantId;
    private Long userId;
    private Long teamId;
    private String nickName;

    public SubApplicantsResponseDto(SubApplicant subApplicant) {

        this.applicantId = subApplicant.getId();
        this.userId = subApplicant.getUser().getId();
        this.teamId = subApplicant.getMatchApplication().getTeam().getId();
        this.nickName = subApplicant.getUser().getNickname();
    }

}
