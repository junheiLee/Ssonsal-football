package com.ssonsal.football.user.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SaveRefreshTokenDto extends SignUpResultDto {

    private String token;

    @Builder
    public SaveRefreshTokenDto(boolean success, int code, String msg, String token) {
        super(success, code, msg);
        this.token = token;
    }

}