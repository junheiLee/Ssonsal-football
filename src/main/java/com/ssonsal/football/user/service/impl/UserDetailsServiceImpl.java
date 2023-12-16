package com.ssonsal.football.user.service.impl;

import com.ssonsal.football.global.exception.CustomException;
import com.ssonsal.football.global.util.ErrorCode;
import com.ssonsal.football.user.entity.User;
import com.ssonsal.football.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.Optional;


@RequiredArgsConstructor
@Slf4j
@Service
public class UserDetailsServiceImpl implements UserDetailsService {


    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) {
        log.info("[loadUserByUsername] loadUserByUsername 수행. email : {}", email);
        Optional<User> user = userRepository.getByEmail(email);
        if (user.isPresent()) {
            User existUser = user.get();
            return existUser;
        } else {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }
    }

}