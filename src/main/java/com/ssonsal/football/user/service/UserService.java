package com.ssonsal.football.user.service;

import com.ssonsal.football.user.dto.SignInRequestDto;
import com.ssonsal.football.user.entity.User;
import org.springframework.validation.Errors;

import java.util.Map;

public interface UserService {
    public Long save(SignInRequestDto dto);

    public User findById(Long userId);

    public User findByEmail(String email);
}
