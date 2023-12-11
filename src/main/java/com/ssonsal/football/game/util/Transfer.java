package com.ssonsal.football.game.util;

import java.util.HashMap;
import java.util.Map;

public class Transfer {

    public static Map<String, Long> longIdToMap(String key, Long value) {

        Map<String, Long> dataDto = new HashMap<>();
        dataDto.put(key, value);
        return dataDto;
    }
}
