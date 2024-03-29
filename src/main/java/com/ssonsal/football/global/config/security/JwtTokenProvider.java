/*
 * JwtTokenProvider
 *
 * Version 1.0.0
 *
 * Date 21.10.24
 */


package com.ssonsal.football.global.config.security;

import com.ssonsal.football.global.exception.CustomException;
import com.ssonsal.football.global.util.ErrorCode;
import com.ssonsal.football.user.entity.User;
import com.ssonsal.football.user.repository.UserRepository;
import com.ssonsal.football.user.service.impl.RedisServiceImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;

/**
 * JWT 토큰을 생성하고 유효성을 검증하는 컴포넌트 클래스 JWT 는 여러 암호화 알고리즘을 제공하고 알고리즘과 비밀키를 가지고 토큰을 생성
 * 유저 정보로 JWT access/refresh 토큰을 생성하고 발급받음
 * claim 정보에는 토큰에 부가적으로 정보를 추가할 수 있음 claim 정보에 회원을 구분할 수 있는 값을 세팅하였다가 토큰이 들어오면 해당 값으로 회원을 구분하여 리소스
 * 제공
 * <p>
 * JWT 토큰에 expire time을 설정할 수 있음
 */

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {


    private final UserDetailsService userDetailsService; // Spring Security 에서 제공하는 서비스 레이어
    private final RedisServiceImpl redisServiceimpl;
    private final RedisTemplate<String, String> redisTemplate;
    private final UserRepository userRepository;
    @Value("${springboot.jwt.secret}")
    private String secretKey;

    @Value("${spring.jwt.token.access-expiration-time}")
    private long accessExpirationTime;

    @Value("${spring.jwt.token.refresh-expiration-time}")
    private long refreshExpirationTime;


    /**
     * SecretKey 에 대해 인코딩 수행
     */
    @PostConstruct
    protected void init() {

        byte[] byteSecretKey = secretKey.getBytes(StandardCharsets.UTF_8);
        secretKey = Base64.getEncoder().encodeToString(byteSecretKey);
    }

    public String createToken(Long userId, Long teamId, int role, long expirationTime, String tokenType) {
        log.info("[createToken] 토큰 생성 시작");
        Claims claims = Jwts.claims().setSubject("userInfo");
        claims.put("id", userId);
        claims.put("teamId", teamId);
        claims.put("role", role);
        claims.put("tokenType", tokenType);

        Date now = new Date();
        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + expirationTime))
                .signWith(SignatureAlgorithm.HS256, secretKey) // 암호화 알고리즘, secret 값 세팅
                .compact();

        if (tokenType.equals("refreshToken")) {// redis에 저장
            redisServiceimpl.setRefreshToken(String.valueOf(userId), token, refreshExpirationTime);
        }
        return token;
    }

    // JWT 토큰으로 인증 정보 조회
    public Authentication getAuthentication(String token) {

        User user = userRepository.findById((this.getUserId(token)))
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        String email = user.getEmail();
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);

        return new UsernamePasswordAuthenticationToken(userDetails, "",
                userDetails.getAuthorities());
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
    }

    public Long getUserId(String token) {
        Claims claims = getClaims(token);
        return claims.get("id", Long.class);
    }

    public Long getTeamId(String token) {
        Claims claims = getClaims(token);
        return claims.get("teamId", Long.class);
    }

    // JWT 토큰에서 회원 구별 정보 추출, getUserEmail로 변경필요
    public String getUsername(String token) {
        log.info("[getUsername] 토큰 기반 회원 구별 정보 추출");
        String info = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody()
                .getSubject();
        log.info("[getUsername] 토큰 기반 회원 구별 정보 추출 완료, info : {}", info);
        return info;
    }

    /**
     * HTTP Request Header 에 설정된 토큰 값을 가져옴
     *
     * @param request Http Request Header
     * @return String type Token 값
     */
    public String resolveToken(HttpServletRequest request) {

//        Cookie[] cookieList = request.getCookies();
//        if(cookieList != null) {
//            String token = getCookieValue(cookieList, "token");
//            log.info("[getCookieValue] HTTP 쿠키에서 Token 값 추출 : {}", token);
//        }
//        log.info("[resolveToken] 헤더정보 전부 출력 : {}",request.getParameterNames());
//
//        log.info("[resolveToken] HTTP 헤더에서 Token 값 추출 3 : {}", request.getHeader("Cookie"));

//        return token;
        log.info("[resloveToken] 헤더에서 값을 제대로 가져오는지 ssonToken : {}", request.getHeader("ssonToken"));//대소문자안먹음
        return request.getHeader("ssonToken");
    }

    // JWT 토큰의 유효성 + 만료일 체크
    public boolean validateToken(String token) {
        log.info("[validateToken] 토큰 유효 체크 시작");
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            log.info("[validateToken] 토큰 유효 체크 완료");
            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            log.info("[validateToken] 토큰 유효 체크 예외 발생");
            return false;
        }
    }

    public String reissue(Long userId, Long teamId) {
        log.info("[reissue] : 토큰 재발급 시작");
        int role = 1;//role 정보도 가져와야 함
        String reAccessToken = createToken(userId, teamId, role, accessExpirationTime, "accessToken");
        log.info("[reissue] : 토큰 재발급 완료 : {}", reAccessToken);
        return reAccessToken;
    }

    public String getCookieValue(Cookie[] cookies, String cookieName) {
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(cookieName)) {
                return cookie.getValue();
            }
        }
        return "";
    }


}