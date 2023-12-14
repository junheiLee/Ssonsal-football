package com.ssonsal.football.user.controller;

import com.ssonsal.football.global.config.security.JwtAuthenticationFilter;
import com.ssonsal.football.global.config.security.JwtTokenProvider;
import com.ssonsal.football.global.util.CookieUtil;
import com.ssonsal.football.global.util.SuccessCode;
import com.ssonsal.football.global.util.formatter.DataResponseBodyFormatter;
import com.ssonsal.football.global.util.formatter.ResponseBodyFormatter;
import com.ssonsal.football.user.dto.*;

import com.ssonsal.football.user.entity.User;
import com.ssonsal.football.user.service.SignService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Tag(name = "Sign Controller", description = "Sign API Controller")
@RestController
@RequestMapping("/user")
public class SignController {


    private final SignService signService; //필드선언 이 컨트롤러에서 SignService를 사용할꺼라고 선언하는거임

    @Autowired
    public SignController(SignService signService) {
        this.signService = signService;
    }

    // SignController 가 실행되면 이 생성자를 호출하고 SignService 빈 객체를 주입받아서 signService로 사용하겠다고 말하는거
//    @Operation(summary = "로그인", description = "email 과 password를 입력해서 로그인 합니다.")
//    @PostMapping(value = "/sign-in")
//    public SignInResultDto signIn(
//
//            @Parameter(description = "Email", required = true) @RequestParam String email,
//            @Parameter(description = "Password 필수값임", required = true) @RequestParam String password)
//            //@Parameter(value = "Password", required = true) String password은 Swagger UI에서 명세화를 진행하면서
//            //@RequestParam String password 한마디로 password 변수가 어떤걸 의미하는지 설명문을 추가해줄수있다
//            //어떤 클래스에서 어떻게 넘어오고 어떠한 용도로 사용되는지도 전부 명시할수있음
//        throws RuntimeException {
//        // signIn 메서드를 호출하고 로그인을 시도하면서 인풋값이 잘못되었을경우 throw RuntimeException 를 던지게된다
//
//        log.info("[signIn] 로그인을 시도하고 있습니다. email : {}, pw : ****",email);
//        SignInResultDto signInResultDto = signService.signIn(email, password);
//
//        if (signInResultDto.getCode() == 0) {
//            log.info("[signIn] 정상적으로 로그인되었습니다. email : {}, token : {}", email,
//                signInResultDto.getToken());
//            log.info("[signIn] signResultDto가 가지고 있는 정보 : {} ",signInResultDto);
//        }
//        return signInResultDto;
//    }
    @PostMapping(value = "/sign-in")
    public  ResponseEntity<ResponseBodyFormatter> signIn(HttpServletRequest req, HttpServletResponse res, @RequestBody SignInRequestDto signInRequestDto){
        log.info("[react 테스트] : {}", signInRequestDto.getEmail());
        log.info("[react 테스트] : {}", signInRequestDto.getPassword());
        SignInResultDto signInResultDto = signService.signIn(signInRequestDto.getEmail(), signInRequestDto.getPassword());
        if (signInResultDto.getCode() == 0) {
            log.info("[signIn] 정상적으로 로그인되었습니다. email : {}, token : {}", signInRequestDto.getEmail(),
                    signInResultDto.getToken());
                    log.info("[signIn] signResultDto가 가지고 있는 정보 : {} ",signInResultDto);
                }
                signService.addAccessTokenToCookie(req,res,signInResultDto.getToken());
                return  DataResponseBodyFormatter.put(SuccessCode.SUCCESS,  signInResultDto.getToken());

            }
            @PostMapping(value = "/sign-up")
            @Operation(summary = "회원가입", description = "회원가입에 필요한 데이터를 입력받아서 회원가입 합니다.")
            public   ResponseEntity<ResponseBodyFormatter> signUp(
                    @Parameter(description = "Email", required = true) @RequestParam String email,
                    @Parameter(description = "비밀번호", required = true) @RequestParam String password,
                    @Parameter(description = "이름", required = true) @RequestParam String name,
                    @Parameter(description = "생년월일", required = true) @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate birth,
                    @Parameter(description = "성별", required = true) @RequestParam String gender,
                    @Parameter(description = "별명", required = true) @RequestParam String nickname,
                    @Parameter(description = "포지션", required = true) @RequestParam String position,
                    @Parameter(description = "핸드폰 번호", required = true) @RequestParam String phone,
                    @Parameter(description = "한줄소개", required = true) @RequestParam String intro,
                    @Parameter(description = "유저 선호 시간", required = true) @RequestParam String preffered_time,
                    @Parameter(description = "유저 선호 지역", required = true) @RequestParam String preffered_area,
            @Parameter(description = "권한", required = true) @RequestParam int role)
            {
                log.info("[signUp] 회원가입을 수행합니다 입값 확인용. email : {}, password : ****, name : {},birth : {}, gender : {}, nickname : {}, position : {}, phone : {}, intro : {}, time : {}. area : {}, role : {}",
                        email, name, birth, gender, nickname, position, phone, intro, preffered_time, preffered_area, role);
                SignUpResultDto signUpResultDto = signService.signUp(email, password, name, birth, gender, nickname,

                position, phone, intro, preffered_time,preffered_area, role);//여기 그냥 SignUpDTO같은걸로 한번에 넘겨주는게 편할듯


                log.info("[signUp] 회원가입을 완료했습니다. Email : {}", email);
                return  DataResponseBodyFormatter.put(SuccessCode.SUCCESS,  signUpResultDto);
            }

            @DeleteMapping(value = "/logout")
            @Operation(summary = "로그아웃", description = "redis 에서 해당 유저의 refreshToken을 삭제합니다.")
            public   ResponseEntity<ResponseBodyFormatter> logOut(@Parameter(description = "Email", required = true) @RequestParam String email
    )throws RuntimeException {
                // signIn 메서드를 호출하고 로그인을 시도하면서 인풋값이 잘못되었을경우 throw RuntimeException 를 던지게된다

                log.info("[logOut] 로그아웃을 시도하고 있습니다. email : {}",email);
                LogOutResultDto logOutResultDto = signService.logOut(email);

                if (logOutResultDto.getCode() == 0) {
                    log.info("[logOut] 정상적으로 로그아웃 되었습니다. email : {}", email);
                    log.info("[logOut] logOutResultDto가 가지고 있는 정보 : {} ",logOutResultDto);
                }
                return  DataResponseBodyFormatter.put(SuccessCode.SUCCESS,  logOutResultDto);
            }
            @GetMapping(value = "/profile")
            @Operation(summary = "프로필", description = "쿠키에있는 토큰정보로 프로필을 표시함")
            public   ResponseEntity<ResponseBodyFormatter> viewProfile(HttpServletRequest req, HttpServletResponse res)throws RuntimeException {
                // signIn 메서드를 호출하고 로그인을 시도하면서 인풋값이 잘못되었을경우 throw RuntimeException 를 던지게된다
                String token = req.getHeader("ssonToken");
                log.info("[profile] 헤더의 토큰정보 확인 token : {}",token);

                return  DataResponseBodyFormatter.put(SuccessCode.SUCCESS,  "쿠키테스트 완료!");
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