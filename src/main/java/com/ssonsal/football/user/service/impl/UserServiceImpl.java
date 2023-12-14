package com.ssonsal.football.user.service.impl;

import com.ssonsal.football.user.dto.SignInRequestDto;
import lombok.RequiredArgsConstructor;
import com.ssonsal.football.user.repository.UserRepository;
import com.ssonsal.football.user.entity.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserServiceImpl {

    private final UserRepository userRepository;

    public Long save(SignInRequestDto dto) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        return userRepository.save(User.builder()
                .email(dto.getEmail())
                .password(encoder.encode(dto.getPassword()))
                .build()).getId();
    }

    public User findById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Unexpected user"));
    }

}
