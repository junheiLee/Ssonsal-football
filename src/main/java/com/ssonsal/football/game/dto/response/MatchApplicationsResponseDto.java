package com.ssonsal.football.game.dto.response;

import com.ssonsal.football.game.entity.MatchApplication;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MatchApplicationsResponseDto {

    private Long id;
    private Long teamId;
    private String name;
    private String uniform;
    private int subCount;
    private LocalDateTime createdAt;

    public MatchApplicationsResponseDto(MatchApplication matchApplication) {
        this.id = matchApplication.getId();
        this.teamId = matchApplication.getTeam().getId();
        this.name = matchApplication.getTeam().getName();
        this.uniform = matchApplication.getUniform();
        this.subCount = matchApplication.getSubCount();
        this.createdAt = matchApplication.getCreatedAt();
    }
}
