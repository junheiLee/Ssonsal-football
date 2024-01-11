package com.ssonsal.football.user.service.impl;

import com.ssonsal.football.user.dto.SignUpRequestDto;
import com.ssonsal.football.user.repository.UserRepository;
import com.ssonsal.football.user.service.AbstractValidator;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.internal.constraintvalidators.AbstractEmailValidator;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
@RequiredArgsConstructor
public class EmailValidator extends AbstractValidator<SignUpRequestDto> {
    private final UserRepository userRepository;
    @Override
    protected void doValidate(SignUpRequestDto signUpRequestDto, Errors errors) {
        if(userRepository.existsByEmail(signUpRequestDto.getEmail())) {
            errors.rejectValue("email", "이메일 중복 오류", "이미 사용중인 이메일 입니다.");
        }
    }
}
