package com.ssonsal.football.team.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Transfer {

    public static Map<String, Object> objectToMap(String key, String secondKey, List teams, String userLevel) {
        Map<String, Object> mapData = new HashMap<>();
        mapData.put(key, teams);
        mapData.put(secondKey, userLevel);

        return mapData;
    }

}
