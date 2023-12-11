package com.ssonsal.football.game.dto.request;

import com.ssonsal.football.game.entity.SubApplicant;
import com.ssonsal.football.team.entity.Team;
import com.ssonsal.football.user.entity.User;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;


@Getter
@Setter
@NoArgsConstructor
@ToString
public class SubApplyListDto {

    private Long userId;
    private String nickName;
    private Long teamId;
    private String subApplicantStatus;

    public SubApplyListDto(List<SubApplicant> subApplicants) {
    }

    public static SubApplyListDto fromSubApplicant(SubApplicant subApplicant) {
        SubApplyListDto dto = new SubApplyListDto();
        dto.setUserId(subApplicant.getUser().getId());
        dto.setNickName(subApplicant.getUser().getNickname());
        dto.setTeamId(subApplicant.getMatchApplication().getTeam().getId());
        dto.setSubApplicantStatus(subApplicant.getSubApplicantStatus());
        return dto;
    }

    public static List<SubApplyListDto> mapSubApplicantsToDto(List<SubApplicant> subApplicants) {
        return subApplicants.stream()
                .map(SubApplyListDto::fromSubApplicant)
                .collect(Collectors.toList());
    }
}
