package com.ssonsal.football.user.service;

public interface RedisService {
    public void setRefreshToken(String userId, String refreshToken, long expiration);

    public void setAccessTokenLogout(String accessToken, long expiration);

    public String getRefreshToken(String refreshToken);

    public String getTokens(String accessToken);

    public void setTokens(String accessToken, String refreshToken, long expiration);

    public boolean deleteRefreshToken(String refreshToken);
}
