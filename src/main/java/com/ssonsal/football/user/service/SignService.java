package com.ssonsal.football.user.service;


import com.ssonsal.football.user.dto.*;
import com.ssonsal.football.user.entity.User;

import java.util.Optional;


public interface SignService {

    SignInResultDto signIn(SignInRequestDto signInRequestDto) throws RuntimeException;

    Optional<User> signUp(SignUpRequestDto signUpRequestDto);

    LogOutResultDto logOut(String email) throws RuntimeException;

    ProfileResultDto viewProfile(String token) throws RuntimeException;

}