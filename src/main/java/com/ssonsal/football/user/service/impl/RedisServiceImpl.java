package com.ssonsal.football.user.service.impl;

import com.ssonsal.football.global.config.security.JwtTokenProvider;
import com.ssonsal.football.user.service.RedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@RequiredArgsConstructor
public class RedisServiceImpl implements RedisService {
    private final RedisTemplate<String, String> redisTemplate;
//    private final JwtTokenProvider jwtTokenProvider;

    // key-value = id- refreshToken
    public void setRefreshToken(String refreshToken, String id, long expiration) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();

        // 만료시간 이후 삭제
        valueOperations.set(refreshToken, id, Duration.ofMinutes(expiration));
        log.info("만료 시간, 분: {}", Duration.ofMinutes(expiration));
    }

    public void setTokens(String accessToken, String refreshToken, long expiration) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();

        // 만료시간 이후 삭제
        valueOperations.set(accessToken,refreshToken, Duration.ofMinutes(expiration));
        log.info("만료 시간, 분: {}", Duration.ofMinutes(expiration));
    }

    // AccessToken 로그아웃
    public void setAccessTokenLogout(String accessToken, long expiration) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(accessToken, "logout", expiration, TimeUnit.MILLISECONDS);
        String expireFormatString = String.format("%d min, %d sec",
                TimeUnit.MILLISECONDS.toMinutes(expiration),
                TimeUnit.MILLISECONDS.toSeconds(expiration) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(expiration))
        );
        log.info("access token 만료 시간, {}", expireFormatString);
    }


    // get RefreshToken
    public String getRefreshToken(String userId) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        log.info("[getRefreshToken] : redis 에서 email로 조회한 refreshToken : {}", valueOperations.get(userId));
        // RefreshToken 없으면 null 반환
        String result = valueOperations.get(userId);
        log.info("getRefreshToken : {}",result.getClass().getName());
        return result;
    }

    // get AccessToken
    public String getTokens(String accessToken) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        return valueOperations.get(accessToken);
    }

    // delete RefreshToken
    public boolean deleteRefreshToken(String userId) {
        // delete 메서드 삭제 시 true 반환
        log.info("[deleteRefreshToken] 요청된 값을 제거합니다 : {}", userId);
        return redisTemplate.delete(userId);
    }
}
