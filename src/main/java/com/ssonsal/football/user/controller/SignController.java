package com.ssonsal.football.user.controller;

import com.ssonsal.football.global.config.security.JwtTokenProvider;
import com.ssonsal.football.global.exception.CustomException;
import com.ssonsal.football.global.util.CookieUtil;
import com.ssonsal.football.global.util.ErrorCode;
import com.ssonsal.football.global.util.SuccessCode;
import com.ssonsal.football.global.util.formatter.DataResponseBodyFormatter;
import com.ssonsal.football.global.util.formatter.ResponseBodyFormatter;
import com.ssonsal.football.user.dto.ProfileResultDto;
import com.ssonsal.football.user.dto.SignInRequestDto;
import com.ssonsal.football.user.dto.SignInResultDto;
import com.ssonsal.football.user.dto.SignUpRequestDto;
import com.ssonsal.football.user.entity.User;
import com.ssonsal.football.user.service.RedisService;
import com.ssonsal.football.user.service.SignService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Tag(name = "Sign Controller", description = "Sign API Controller")
public class SignController {


    private final SignService signService;
    private final RedisService redisService;
    private final JwtTokenProvider jwtTokenProvider;
    private final CookieUtil cookieUtil;

    /**
     * 로그인 기능
     * 시큐리티에서 서비스를 요청한 유저정보가 없을시 강제적으로 이동됨
     *
     * @param request          CookieUtil을 사용하기 위해 필요한 HttpServletRequest
     * @param response         CookieUtil을 사용하기 위해 필요한 HttpServletRequest
     * @param signInRequestDto 유저가 로그인하기 위해 입력한 ID, Password
     * @return 로그인한 유저 객체,발급받은 accessToken ,refreshToken 반환
     */
    @Operation(summary = "로그인", description = "email 과 password를 입력해서 로그인 합니다.")
    @PostMapping(value = "/sign-in")
    public ResponseEntity<ResponseBodyFormatter> signIn(HttpServletRequest request, HttpServletResponse response, @RequestBody SignInRequestDto signInRequestDto) {

        SignInResultDto signInResultDto = signService.signIn(signInRequestDto);

        if (signInResultDto != null) {
            log.info("[signIn] 정상적으로 로그인되었습니다. email : {}, token : {}", signInRequestDto.getEmail(),
                    signInResultDto.getId());
            log.info("[signIn] signResultDto가 가지고 있는 정보 : {} ", signInResultDto);
        }
        cookieUtil.addAccessTokenToCookie(request, response, signInResultDto.getAccessToken());
        return DataResponseBodyFormatter.put(SuccessCode.SUCCESS, signInResultDto);

    }

    /**
     * 회원가입 기능
     *
     * @param signUpRequestDto 회원가입 폼을 통해서 입력된 데이터
     * @return 성공 메세지
     * @throws RuntimeException
     */
    @PostMapping(value = "/sign-up")
    @Operation(summary = "회원가입", description = "회원가입에 필요한 데이터를 입력받아서 회원가입 합니다.")
    public ResponseEntity<ResponseBodyFormatter> signUp(@RequestBody SignUpRequestDto signUpRequestDto) throws RuntimeException {
        log.info("[signUp] 회원가입 입력값 확인 : {}", signUpRequestDto);
        Optional<User> user = signService.signUp(signUpRequestDto);
        if (user.isPresent()) {
            User newUser = user.get();
            log.info("[signUp] 회원가입을 완료했습니다. UserId : {}", newUser);
            return DataResponseBodyFormatter.put(SuccessCode.SUCCESS, "회원가입 성공");
        } else {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }


    }

    /**
     * 로그아웃 기능
     * 로그아웃을 진행한 뒤, redis에 들어있는 해당 유저의 refresh토큰을 제거한다.
     *
     * @return 성공 메세지
     * @throws RuntimeException
     */
    @PostMapping(value = "/logout")
    @Operation(summary = "로그아웃", description = "redis 에서 해당 유저의 refreshToken을 삭제합니다.")
    public ResponseEntity<ResponseBodyFormatter> logOut(HttpServletRequest request, HttpServletResponse response) throws RuntimeException {
        // signIn 메서드를 호출하고 로그인을 시도하면서 인풋값이 잘못되었을경우 throw RuntimeException 를 던지게된다

        String token = request.getHeader("ssonToken");
        String logoutResult = signService.logOut(token);
        new SecurityContextLogoutHandler().logout(request, response, SecurityContextHolder.getContext().getAuthentication());
        if (logoutResult.equals("success")) {

            cookieUtil.deleteCookie(request, response, "ssonToken");

            return DataResponseBodyFormatter.put(SuccessCode.SUCCESS, "success");
        } else
            return DataResponseBodyFormatter.put(SuccessCode.SUCCESS, "fail");
    }

    @PostMapping("/user/refresh-token")
    public ResponseEntity<ResponseBodyFormatter> refreshToken(HttpServletRequest request, HttpServletResponse response) {

        log.info("[refresh-token] accessToken 만료 재발급 시작");
        log.info("[doFilterInternal] accessToken으로 refreshToken 조회 ");
        String token = request.getHeader("ssonToken");
        String refreshToken = redisService.getTokens(token);
        log.info("[doFilterInternal] redis에서 가져온 refreshToken : {}", refreshToken);

        if (jwtTokenProvider.validateToken(refreshToken)) {
            log.info("[doFilterInternal] redis에서 가져온 refreshToken 값 유효성 체크 완료");
            log.info("[doFilterInternal] 새로운 accessToken발급후 인증객체 등록");
            String newAccessToken = jwtTokenProvider.reissue(jwtTokenProvider.getUserId(refreshToken), jwtTokenProvider.getTeamId(refreshToken));
            Authentication authentication = jwtTokenProvider.getAuthentication(newAccessToken);
            // 토큰이 유효하면 인증객체를 SecurityContestHolder에 저장한다
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // 새로운 accessToken을 클라이언트에게 전달

            response.setHeader("ssonToken", newAccessToken);
            // 리다이렉트 URL을 클라이언트에게 전달


            return DataResponseBodyFormatter.put(SuccessCode.SUCCESS, newAccessToken);

        } else {
            // refreshToken이 유효하지 않으면 401 Unauthorized 응답을 보냄
            return DataResponseBodyFormatter.put(SuccessCode.SUCCESS, "fail");
        }
    }

    /**
     * @param req Cookie 정보가 담겨있는 request요청
     * @return
     * @throws RuntimeException
     */
    @GetMapping(value = "/profile")
    @Operation(summary = "프로필", description = "쿠키에있는 토큰정보로 프로필을 표시함")
    public ResponseEntity<ResponseBodyFormatter> myProfile(HttpServletRequest req) throws RuntimeException {
        // signIn 메서드를 호출하고 로그인을 시도하면서 인풋값이 잘못되었을경우 throw RuntimeException 를 던지게된다
        String token = req.getHeader("ssonToken");
        ProfileResultDto profileResultDto = signService.viewProfile(token);
        log.info("[profile] 헤더의 토큰정보 확인 token : {}", token);


        return DataResponseBodyFormatter.put(SuccessCode.SUCCESS, profileResultDto);
    }


    @GetMapping(value = "/exception")
    public void exceptionTest() throws RuntimeException {
        throw new RuntimeException("접근이 금지되었습니다.");
    }

    @ExceptionHandler(value = RuntimeException.class)
    public ResponseEntity<Map<String, String>> ExceptionHandler(RuntimeException e) {
        HttpHeaders responseHeaders = new HttpHeaders();
        //responseHeaders.add(HttpHeaders.CONTENT_TYPE, "application/json") ;
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

        log.error("ExceptionHandler 호출, {}, {}", e.getCause(), e.getMessage());

        Map<String, String> map = new HashMap<>();
        map.put("error type", httpStatus.getReasonPhrase());
        map.put("code", "400");
        map.put("message", "에러 발생");

        return new ResponseEntity<>(map, responseHeaders, httpStatus);
    }

    private Map<String, Long> dataToMap(String key, Long value) {

        Map<String, Long> dataDto = new HashMap<>();
        dataDto.put(key, value);
        return dataDto;
    }
}