package com.ssonsal.football.global.config.security;

import com.ssonsal.football.user.service.impl.RedisServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * 어플리케이션의 보안 설정
 *
 * @author jung
 * @version 1.0.0
 */

@Configuration
@Slf4j
//@EnableWebSecurity // Spring Security에 대한 디버깅 모드를 사용하기 위한 어노테이션 (default : false)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    //WebSecurityConfigurerAdapter 가 deprecated SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> 또는
    //configure 메서드를 오버라이딩하여 설정을 진행하는 것이 아닌 설정들을 하나의 Bean으로 등록하는 Component화 하는등의 리팩토링 필요

    private final JwtTokenProvider jwtTokenProvider;
    private final RedisServiceImpl redisService;
    @Value("${spring.jwt.token.access-expiration-time}")
    private long accessExpirationTime;

    @Value("${spring.jwt.token.refresh-expiration-time}")
    private long refreshExpirationTime;

    @Autowired
    public SecurityConfiguration(JwtTokenProvider jwtTokenProvider, RedisServiceImpl redisService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.redisService = redisService;
    }


    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception { // SecurityFilter 으로 변경 필요함
        httpSecurity.httpBasic().disable() // REST API는 UI를 사용하지 않으므로 기본설정을 비활성화

                .csrf().disable() // REST API는 csrf 보안이 필요 없으므로 비활성화ㅂ

                .sessionManagement()
                .sessionCreationPolicy(
                        SessionCreationPolicy.STATELESS) // JWT Token 인증방식으로 세션은 필요 없으므로 비활성화
                .and()
                .authorizeRequests() // 리퀘스트에 대한 사용권한 체크
                .antMatchers("/user/sign-in", "/user/sign-up",
                        "/user/exception").permitAll() // 가입 및 로그인 주소는 허용
                .antMatchers("**exception**").permitAll()
                .anyRequest().authenticated()
                .and()
                .exceptionHandling().accessDeniedHandler(new CustomAccessDeniedHandler())
                .and()
                .exceptionHandling().authenticationEntryPoint(new CustomAuthenticationEntryPoint())

                .and()
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider, redisService),
                        UsernamePasswordAuthenticationFilter.class); // JWT Token 필터를 id/password 인증 필터 이전에 추가


    }

}
/**
 * Swagger 페이지 접근에 대한 예외 처리
 *
 */