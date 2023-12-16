package com.ssonsal.football.user.entity;

import lombok.*;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

import javax.persistence.Id;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
//@Entity
@RedisHash(value = "refresh_token")
public class RefreshToken {

    @Id
    private Long id;

    @Indexed // 값으로 검색하기 위함
    private String email;

    private String role;

    private String refreshToken;

    @TimeToLive // 만료시간
    private Long ttl;//Time To Live

    public RefreshToken(String email, String refreshToken) {
        this.email = email;
        this.refreshToken = refreshToken;
    }

    public RefreshToken update(String newRefreshToken,Long ttl) {
        this.refreshToken = newRefreshToken;
        this.ttl = ttl;
        return this;
    }
}

