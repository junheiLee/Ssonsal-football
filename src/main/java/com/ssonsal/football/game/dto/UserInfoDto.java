package com.ssonsal.football.game.dto;

import lombok.*;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserInfoDto {

    private Long userId;
    private Long teamId;

    @Builder
    public UserInfoDto(Long userId, Long teamId) {
        this.userId = userId;
        this.teamId = teamId;
    }
}
