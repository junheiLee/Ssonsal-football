package com.ssonsal.football.user.dto;

import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class SignUpRequestDto {

    private String email;

    private String password;

    private String name;

    private LocalDate birth;

    private int gender;

    private String nickname;

    private String position;

    private String phone;

    private String preferredTime;

    private String preferredArea;

    private String intro;

    private int role;
}