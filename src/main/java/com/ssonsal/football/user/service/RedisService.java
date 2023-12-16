package com.ssonsal.football.user.service;

public interface RedisService {
    public void setRefreshToken(String refreshToken, String email, long expiration);
    public void setAccessTokenLogout(String accessToken, long expiration);
    public String getRefreshToken(String refreshToken);
    public String getAccessToken(String accessToken);
    public void deleteRefreshToken(String refreshToken);
}
