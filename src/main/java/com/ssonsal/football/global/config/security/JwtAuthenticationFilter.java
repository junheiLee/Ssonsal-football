package com.ssonsal.football.global.config.security;

import com.ssonsal.football.user.service.impl.RedisServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {


    private final JwtTokenProvider jwtTokenProvider;
    private final RedisServiceImpl redisService;
    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider, RedisServiceImpl redisService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.redisService = redisService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest servletRequest,
                                    HttpServletResponse servletResponse,
                                    FilterChain filterChain) throws ServletException, IOException {

        Cookie[] cookieList = servletRequest.getCookies();
        if(cookieList != null) {
            String cookieToken = jwtTokenProvider.getCookieValue(cookieList, "token");
            log.info("[getCookieValue] HTTP 쿠키에서 Token 값 추출 : {}", cookieToken);
            log.info("[setHeader] 헤더에 추출한 토큰 정보 추가 ");
            servletResponse.setHeader("ssonToken",cookieToken);
        }
        String token = jwtTokenProvider.resolveToken(servletRequest);
        log.info("[doFilterInternal] accessToken 값 추출 완료. token : {}", token);
        log.info("[doFilterInternal] accessToken 값 유효성 체크 시작");
//        if (token != null && jwtTokenProvider.validateToken(token)) {
//            Authentication authentication = jwtTokenProvider.getAuthentication(token);
//            SecurityContextHolder.getContext().setAuthentication(authentication);
//            log.info("[doFilterInternal] token 값 유효성 체크 완료");
//        }
        if(token == null){
            log.info("[doFilterInternal] Token 없음");
            //로그인 페이지로 보내야함
        }else if (token != null && jwtTokenProvider.validateToken(token)) {
            Authentication authentication = jwtTokenProvider.getAuthentication(token);
            // 토큰이 유효하면 인증객체를 SecurityContestHolder에 저장한다
            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.info("[doFilterInternal] token 값 유효성 체크 완료");
        }else{
            //accessToken값이 유효하지 않기때문에 redis에 저장되어있는 refreshToken의 이 유효한지를 확인한다.
            //먼저 레디스에 저장되어있는 토큰값을 들고와야함
            log.info("[doFilterInternal] accessToken 만료 재발급 시작");
            log.info("[doFilterInternal] accessToken으로 refreshToken 조회 ");
            String refreshToken = redisService.getTokens(token);
            log.info("[doFilterInternal] redis에서 가져온 refreshToken : {}", refreshToken);
            if(jwtTokenProvider.validateToken(refreshToken)){
                log.info("[doFilterInternal] redis에서 가져온 refreshToken 값 유효성 체크 완료");
                log.info("[doFilterInternal] 새로운 accessToken발급후 인증객체 등록");
                String newAccessToken = jwtTokenProvider.reissue(jwtTokenProvider.getUserId(refreshToken), jwtTokenProvider.getTeamId(refreshToken));
                servletResponse.setHeader("ssonToken",newAccessToken);
            Authentication authentication = jwtTokenProvider.getAuthentication(newAccessToken);
            // 토큰이 유효하면 인증객체를 SecurityContestHolder에 저장한다
            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.info("[doFilterInternal] 새로운 accessToken발급후 인증객체 등록완료");
            }
        }


        filterChain.doFilter(servletRequest, servletResponse);
    }
}