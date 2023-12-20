package com.ssonsal.football.user.dto;

import lombok.*;


import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class SignUpRequestDto {

    @Email(message = "이메일 형식이 아닙니다. ")
    private String email;

    @Pattern(regexp="^(?=.*[a-zA-Z])(?=.*[!@#$%^*+=-])(?=.*[0-9]).{8,25}$", message = "영문, 숫자포함 8자리를 입력해주세요." )
    private String password;

    @Size(min=2, max=8, message="2자 부터 8자 사이의 이름을 입력해 주세요")
    private String name;

    @Past
    private LocalDate birth;

    private String gender;

    @NotEmpty
    private String nickname;

    private String position;

    @Pattern(regexp="\\d{3}-\\d{3,4}-\\d{4}", message = "전화번호 형식으로 입력해 주세요")
    private String phone;

    @Size(max=1000)
    private String intro;

    private String preferredTime;

    private String preferredArea;

    private int role;
}