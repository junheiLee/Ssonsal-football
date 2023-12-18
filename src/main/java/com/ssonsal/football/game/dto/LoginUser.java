package com.ssonsal.football.game.dto;

import lombok.*;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LoginUser {

    private Long id;
    private Long teamId;

    @Builder
    public LoginUser(Long userId, Long teamId) {
        this.id = userId;
        this.teamId = teamId;
    }
}
