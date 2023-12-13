package com.ssonsal.football.user.service.impl;

import com.ssonsal.football.global.config.CommonResponse;
import com.ssonsal.football.global.config.security.JwtTokenProvider;
import com.ssonsal.football.global.util.CookieUtil;
import com.ssonsal.football.user.dto.LogOutResultDto;
import com.ssonsal.football.user.dto.SaveRefreshTokenDto;
import com.ssonsal.football.user.dto.SignInResultDto;
import com.ssonsal.football.user.dto.SignUpResultDto;
import com.ssonsal.football.user.entity.User;
import com.ssonsal.football.user.repository.UserRepository;
import com.ssonsal.football.user.service.SignService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;


@Slf4j
@Service
public class SignServiceImpl implements SignService {


    public UserRepository userRepository;
    public JwtTokenProvider jwtTokenProvider;
    public PasswordEncoder passwordEncoder;
    @Value("${spring.jwt.token.access-expiration-time}")
    private long accessTokenValid; // 1시간 토큰 유효
    @Value("${spring.jwt.token.refresh-expiration-time}")
    private long refreshTokenValid; // 3시간 토큰 유효

    @Autowired
    public SignServiceImpl(UserRepository userRepository, JwtTokenProvider jwtTokenProvider,
                           PasswordEncoder passwordEncoder, CookieUtil cookieUtil) {
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public SignUpResultDto signUp(String email, String password, String name, LocalDate birth,
                                  String gender, String nickname, String position, String phone,
                                  String intro, String preffered_time, String preffered_area, int role) {
        log.info("[getSignUpResult] 회원 가입 정보 전달");
        User user;
        if (role == 1) {
            user = User.builder()
                    .email(email)
                    .password(passwordEncoder.encode(password))
                    .name(name)
                    .birth(birth)
                    .gender(gender)
                    .nickname(nickname)
                    .position(position)
                    .phone(phone)
                    .intro(intro)
                    .preferredTime(preffered_time)
                    .preferredArea(preffered_area)
                    .role(1)
                    .build();
        } else if (role == 0) {
            user = User.builder()
                    .email(email)
                    .password(passwordEncoder.encode(password))
                    .name(name)
                    .birth(birth)
                    .gender(gender)
                    .nickname(nickname)
                    .position(position)
                    .phone(phone)
                    .intro(intro)
                    .preferredTime(preffered_time)
                    .preferredArea(preffered_area)
                    .role(0)
                    .build();
        } else if (role == 2) {
            user = User.builder()
                    .email(email)
                    .password(passwordEncoder.encode(password))
                    .name(name)
                    .birth(birth)
                    .gender(gender)
                    .nickname(nickname)
                    .position(position)
                    .phone(phone)
                    .intro(intro)
                    .preferredTime(preffered_time)
                    .preferredArea(preffered_area)
                    .role(2)
                    .build();
        } else {
            user = User.builder()
                    .email(email)
                    .password(passwordEncoder.encode(password))
                    .name(name)
                    .birth(birth)
                    .gender(gender)
                    .nickname(nickname)
                    .position(position)
                    .phone(phone)
                    .intro(intro)
                    .preferredTime(preffered_time)
                    .preferredArea(preffered_area)
                    .role(4)
                    .build();
        }

        log.info("[userSave] builder 패턴으로 만든 User객체 확인용 : {}",user.toString());
        User savedUser = userRepository.save(user);
        log.info("[userSave] builder 패턴으로 만든 User객체 저장완료 : {}",savedUser);
        SignUpResultDto signUpResultDto = new SignInResultDto();

        log.info("[getSignUpResult] userEntity 값이 들어왔는지 확인 후 결과값 주입");
        if (!savedUser.getName().isEmpty()) {
            log.info("[getSignUpResult] 정상 처리 완료");
            setSuccessResult(signUpResultDto);
        } else {
            log.info("[getSignUpResult] 실패 처리 완료");
            setFailResult(signUpResultDto);
        }
        return signUpResultDto;
    }

    @Override
    public SignInResultDto signIn(String email, String password) throws RuntimeException {
        log.info("[getSignInResult] signDataHandler 로 회원 정보 요청");
        User user = userRepository.getByEmail(email);
        log.info("[getSignInResult] Email : {}", email);

        log.info("[getSignInResult] 패스워드 비교 수행");
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException();
        }
        log.info("[getSignInResult] 패스워드 일치");

        log.info("[accessToken] accessToken 객체 생성");
        SignInResultDto signInResultDto = SignInResultDto.builder()
                .token(jwtTokenProvider.generateToken(String.valueOf(user.getEmail()),accessTokenValid,
                        user.getRole(),"access"))
                .build();
        log.info("[saveRefreshToken] refreshToken 객체 생성");
        SaveRefreshTokenDto saveRefreshTokenDto = SaveRefreshTokenDto.builder()
                .token(jwtTokenProvider.generateToken(String.valueOf(user.getEmail()),refreshTokenValid,
                        user.getRole(),"refresh"))
                .build();

        log.info("[getSignInResult] SignInResultDto 객체에 값 주입 : {}", signInResultDto.toString());
        log.info("[getSignInResult] SignInResultDto 객체에 값 주입 : {}", signInResultDto.toString());
        setSuccessResult(signInResultDto);

        return signInResultDto;
    }
    @Override
    public LogOutResultDto logOut(String email) throws RuntimeException{
        log.info("[logOut] signDataHandler로 회원 정보 요청 ");
        log.info("[logOut] Email : {}", email);
        log.info("[logOut] Email 정보로 redis에서 토큰 블랙리스트 처리");

        log.info("[removeRefreshToken] ");
        log.info("[logOut] Email 정보로 redis에서 토큰 제거");
        log.info("[BlackedToken] ");
        LogOutResultDto logOutResultDto = new LogOutResultDto();

//        if (!redis.getRefreshToken().isEmpty()) {
//            log.info("[removeToken] 정상 처리 완료");
//            setSuccessResult(logOutResultDto);
//        } else {
//            log.info("[removeToken] 실패 처리 완료");
//            setFailResult(logOutResultDto);
//        }
        log.info("[logOut] 작업 완료");
        return logOutResultDto;
    }

    // 결과 모델에 api 요청 성공 데이터를 세팅해주는 메소드
    private void setSuccessResult(SignUpResultDto result) {
        result.setSuccess(true);
        result.setCode(CommonResponse.SUCCESS.getCode());
        result.setMsg(CommonResponse.SUCCESS.getMsg());
    }

    // 결과 모델에 api 요청 실패 데이터를 세팅해주는 메소드
    private void setFailResult(SignUpResultDto result) {
        result.setSuccess(false);
        result.setCode(CommonResponse.FAIL.getCode());
        result.setMsg(CommonResponse.FAIL.getMsg());
    }
    public void addAccessTokenToCookie(HttpServletRequest request, HttpServletResponse response, String accessToken) throws RuntimeException{
        CookieUtil.deleteCookie(request, response, "token");
        CookieUtil.addCookie(response, "token", accessToken);
    }
}