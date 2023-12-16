package com.ssonsal.football.game.dto;

import lombok.*;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LoginUserInfoDto {

    private Long userId;
    private Long teamId;

    @Builder
    public LoginUserInfoDto(Long userId, Long teamId) {
        this.userId = userId;
        this.teamId = teamId;
    }
}
