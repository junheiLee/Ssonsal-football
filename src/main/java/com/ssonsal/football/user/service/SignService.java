package com.ssonsal.football.user.service;


import com.ssonsal.football.user.dto.SignInResultDto;
import com.ssonsal.football.user.dto.SignUpResultDto;

import java.time.LocalDate;

// 예제 13.24
public interface SignService {

    SignUpResultDto signUp(String email, String password, String name, LocalDate birth, int gender, String nickname, String position, String phone, String intro, String preffered_time, String preffered_area, int role);

    SignInResultDto signIn(String email, String password) throws RuntimeException;

}