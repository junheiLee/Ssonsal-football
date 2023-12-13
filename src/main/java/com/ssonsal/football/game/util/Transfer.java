package com.ssonsal.football.game.util;

import com.ssonsal.football.game.dto.LoginUserInfoDto;

import java.util.HashMap;
import java.util.Map;

import static com.ssonsal.football.game.util.GameConstant.USER_INFO;

public class Transfer {

    public static Map<String, Long> longIdToMap(String key, Long value) {

        Map<String, Long> dataDto = new HashMap<>();
        dataDto.put(key, value);
        return dataDto;
    }

    public static Map<String, Object> objectToMap(String key, Object dto) {
        Map<String, Object> mapData = new HashMap<>();
        mapData.put(key, dto);

        return mapData;
    }

    public static Map<String, Object> toMapIncludeUserInfo(Long userId, Long teamId, String key, Object dto) {

        Map<String, Object> mapData = new HashMap<>();
        LoginUserInfoDto loginUserInfoDto = LoginUserInfoDto.builder()
                .userId(userId)
                .teamId(teamId)
                .build();

        mapData.put(USER_INFO, loginUserInfoDto);
        mapData.put(key, dto);

        return mapData;
    }
}
