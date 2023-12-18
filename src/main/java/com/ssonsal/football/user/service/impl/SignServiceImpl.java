package com.ssonsal.football.user.service.impl;

import com.ssonsal.football.global.config.security.JwtTokenProvider;
import com.ssonsal.football.global.exception.CustomException;
import com.ssonsal.football.global.util.ErrorCode;
import com.ssonsal.football.user.dto.ProfileResultDto;
import com.ssonsal.football.user.dto.SignInRequestDto;
import com.ssonsal.football.user.dto.SignInResultDto;
import com.ssonsal.football.user.dto.SignUpRequestDto;
import com.ssonsal.football.user.entity.User;
import com.ssonsal.football.user.repository.UserRepository;
import com.ssonsal.football.user.service.RedisService;
import com.ssonsal.football.user.service.SignService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;


@Slf4j
@Service
@RequiredArgsConstructor
public class SignServiceImpl implements SignService {


    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final RedisService redisService;


    @Value("${spring.jwt.token.access-expiration-time}")
    private long accessTokenValid; // 1시간 토큰 유효
    @Value("${spring.jwt.token.refresh-expiration-time}")
    private long refreshTokenValid; // 3시간 토큰 유효

    @Override
    @Transactional
    public SignInResultDto signIn(SignInRequestDto signInRequestDto) throws RuntimeException {
        log.info("[getSignInResult] signDataHandler 로 회원 정보 요청");
        Optional<User> user = userRepository.getByEmail(signInRequestDto.getEmail());

        if (user.isPresent()) {
            User existUser = user.get();
            log.info("[getSignInResult] 해당 계정이 존재합니다 : {}", signInRequestDto.getEmail());
            log.info("[getSignInResult] 해당 계정이 존재합니다 : {}", existUser);
            log.info("[getSignInResult] 패스워드 비교 수행");
            if (!passwordEncoder.matches(signInRequestDto.getPassword(), existUser.getPassword())) {
                throw new CustomException(ErrorCode.WRONG_PASSWORD);
            }
            log.info("[getSignInResult] 패스워드 일치");
            log.info("[accessToken] accessToken 토큰 생성");
            String accessToken = jwtTokenProvider.generateToken(existUser.getId(),
                    ((existUser.getTeam() != null) ? existUser.getTeam().getId() : 0L),
                    accessTokenValid,
                    existUser.getRole(), "accessToken");
            log.info("[accessToken] accessToken 토큰 확인 : {}", accessToken);
            log.info("[refreshToken] refreshToken 토큰 생성");
            String refreshToken = jwtTokenProvider.generateToken(existUser.getId(),
                    ((existUser.getTeam() != null) ? existUser.getTeam().getId() : 0L),
                    refreshTokenValid,
                    existUser.getRole(), "refreshToken");
            log.info("[refreshToken] refreshToken 토큰 확인 : {}", refreshToken);
            log.info("[saveTokens] 재발행을 위한 토큰 저장 만료일은 refresh와 동일 ");
            redisService.setTokens(accessToken, refreshToken, refreshTokenValid);
            log.info("[signInResultDto] 작성 시작");
            SignInResultDto signInResultDto = new SignInResultDto(existUser, accessToken, refreshToken);
            log.info("[signInResultDto] 확인 : {}", signInResultDto);
            return signInResultDto;
        } else {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }

    }

    @Override
    @Transactional
    public Optional<User> signUp(SignUpRequestDto signUpRequestDto) {
        log.info("[signUpService] 회원가입을 수행합니다. 입력값 확인 : {}", signUpRequestDto);

        try {
            User user = User.builder()
                    .email(signUpRequestDto.getEmail())
                    .password(passwordEncoder.encode(signUpRequestDto.getPassword()))
                    .name(signUpRequestDto.getName())
                    .birth(signUpRequestDto.getBirth())
                    .gender(signUpRequestDto.getGender())
                    .nickname(signUpRequestDto.getNickname())
                    .position(signUpRequestDto.getPosition())
                    .phone(signUpRequestDto.getPhone())
                    .intro(signUpRequestDto.getIntro())
                    .preferredTime(signUpRequestDto.getPreferredTime())
                    .preferredArea(signUpRequestDto.getPreferredArea())
                    .role(signUpRequestDto.getRole())
                    .build();


            log.info("[signUpService] User 객체 생성 확인 - {}", user.toString());

            User savedUser = userRepository.save(user);

            if (savedUser == null) {
                throw new RuntimeException("회원 가입 중 오류가 발생하였습니다. (저장된 사용자 정보가 null입니다.)");
            }

            log.info("[signUpService] User 객체 저장 완료 - {}", savedUser.toString());

            return Optional.ofNullable(savedUser);
        } catch (DataAccessException e) {
            throw new RuntimeException("회원 가입 중 오류가 발생하였습니다. (데이터 액세스 예외)", e);
        }
    }

    @Override
    @Transactional
    public String logOut(String token) throws RuntimeException {
        log.info("[logOut] signDataHandler로 회원 정보 요청 하기 위한 토큰 : {}", token);
        Long userId = jwtTokenProvider.getUserId(token);
        log.info("[logOut] userId : {}", userId);
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            log.info("[logout] 유저ID로 검색한 유저 정보 : {}", user.get().getId());
            log.info("[logOut] Email 정보로 redis에서 토큰 블랙리스트 처리");
            log.info("[removeRefreshToken] ");
            log.info("[logOut] Email 정보로 redis에서 토큰 제거");
            Boolean deleteResult = redisService.deleteRefreshToken(user.get().getId().toString());
            if (deleteResult) {
                log.info("[removeToken] 정상 처리 완료");
                log.info("[logOut] 작업 완료");
                return "success";
            } else {
                log.info("[Logout] Error");
                return "fail";
            }

        } else {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }
        //User user = userRepository.getByEmail(email);
    }

    @Override
    public ProfileResultDto viewProfile(String token) throws RuntimeException {
        Long userId = jwtTokenProvider.getUserId(token);
        log.info("[viewPofile] : 토큰에서 유저ID가져옴 : {}", userId);
        log.info("[viewPofile] 유저 정보 검색 시작");
        Optional<User> user = userRepository.findById(userId);
        log.info("[viewPofile] 유저ID로 검색한 유저 정보 Optional : {}", user);
        if (user.isPresent()) {
            log.info("[viewPofile] 유저ID로 검색한 유저 정보 : {}", user.get());
            log.info("[viewPofile] Dto 생성결과 : {}", new ProfileResultDto(user.get()));
            return new ProfileResultDto(user.get());
        } else {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }
    }


}