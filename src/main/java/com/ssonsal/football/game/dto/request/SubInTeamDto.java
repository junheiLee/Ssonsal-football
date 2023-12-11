package com.ssonsal.football.game.dto.request;

import com.ssonsal.football.game.entity.Sub;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class SubInTeamDto {

    private Long userId;
    private String userName;
    private LocalDateTime createAt;

    public static SubInTeamDto mapToSubInTeamDto(Sub sub) {
        SubInTeamDto subRecordDto = new SubInTeamDto();
        subRecordDto.setUserId(sub.getUser().getId());
        subRecordDto.setUserName(sub.getUser().getName());
        subRecordDto.setCreateAt(sub.getCreatedAt());
        return subRecordDto;
    }

}
