package com.ssonsal.football.global.util.transfer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import static com.ssonsal.football.game.util.GameConstant.SCHEDULE_FORMAT;

public class Transfer {

    public static Map<String, Long> longIdToMap(String key, Long value) {

        Map<String, Long> dataDto = new HashMap<>();
        dataDto.put(key, value);
        return dataDto;
    }

    public static Map<String, Object> toMap(String key, Object dto) {
        Map<String, Object> mapData = new HashMap<>();
        mapData.put(key, dto);

        return mapData;
    }

    public static String toStringSchedule(LocalDateTime schedule) {
        return schedule.format(DateTimeFormatter.ofPattern(SCHEDULE_FORMAT));
    }
}
