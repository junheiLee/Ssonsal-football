package com.ssonsal.football.user.service;


import com.ssonsal.football.user.dto.ProfileResultDto;
import com.ssonsal.football.user.dto.SignInRequestDto;
import com.ssonsal.football.user.dto.SignInResultDto;
import com.ssonsal.football.user.dto.SignUpRequestDto;
import com.ssonsal.football.user.entity.User;
import org.springframework.validation.Errors;

import java.util.Map;
import java.util.Optional;


public interface SignService {

    SignInResultDto signIn(SignInRequestDto signInRequestDto) throws RuntimeException;

    Optional<User> signUp(SignUpRequestDto signUpRequestDto);

    String logOut(String email) throws RuntimeException;

    ProfileResultDto viewProfile(Long userId);
    Map<String, String> validateHandling(Errors errors);

}