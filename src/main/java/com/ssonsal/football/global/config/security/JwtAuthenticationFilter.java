package com.ssonsal.football.global.config.security;

import com.ssonsal.football.global.exception.CustomAccessDeniedException;
import com.ssonsal.football.global.util.ErrorCode;
import com.ssonsal.football.user.service.impl.RedisServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.core.GrantedAuthority;


import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;


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
        if (cookieList != null) {
            String cookieToken = jwtTokenProvider.getCookieValue(cookieList, "token");

            servletResponse.setHeader("ssonToken", cookieToken);
        }
        String token = jwtTokenProvider.resolveToken(servletRequest);

        if (token == null) {
            log.info("[doFilterInternal] Token 없음");

        } else if (jwtTokenProvider.validateToken(token)) {
            Authentication authentication = jwtTokenProvider.getAuthentication(token);
            // 토큰이 유효하면 인증객체를 SecurityContestHolder에 저장한다
            log.info("여기 JwtAuthenticationFilter 48줄 근데 authentication={}", authentication);
            log.info(authentication.getName());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            // 인증 객체의 권한을 확인한다
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            List<String> roles = userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());

            if(!roles.contains("user")){
                SecurityContextHolder.clearContext(); // 인증 정보를 제거
                throw new CustomAccessDeniedException(String.valueOf(ErrorCode.NOT_PERMISSION));
            }
        } else {

            String refreshToken = redisService.getTokens(token);
            if (jwtTokenProvider.validateToken(refreshToken)) {

                String newAccessToken = jwtTokenProvider.reissue(jwtTokenProvider.getUserId(refreshToken), jwtTokenProvider.getTeamId(refreshToken));
                servletResponse.setHeader("ssonToken", newAccessToken);
                Authentication authentication = jwtTokenProvider.getAuthentication(newAccessToken);
                // 토큰이 유효하면 인증객체를 SecurityContestHolder에 저장한다
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }


        filterChain.doFilter(servletRequest, servletResponse);
    }
}