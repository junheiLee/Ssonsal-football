package com.ssonsal.football.team.entity;

import lombok.Getter;

/**
 * 팀 평균 연령대에 따라 다른 값을 주기위한 클래스
 */
@Getter
public enum AgeFormatter {

    EARLY("대 초반"),
    MID("대 중반"),
    LATE("대 후반");

    private final String ageGroup;

    AgeFormatter(String ageGroup) {
        this.ageGroup = ageGroup;
    }

}
