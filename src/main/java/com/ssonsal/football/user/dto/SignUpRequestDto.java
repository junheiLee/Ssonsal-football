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

    private String gender;

    private String nickname;

    private String position;

    private String phone;

    private String intro;

    private String preferredTime;

    private String preferredArea;

    private int role;
}