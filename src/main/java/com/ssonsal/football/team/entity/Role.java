package com.ssonsal.football.team.entity;

import lombok.Getter;

/**
 * 유저별 권한에 따라 다른 값을 주기위한 클래스
 */
@Getter
public enum Role {

    GUEST("게스트"),
    USER("유저"),
    TEAM_LEADER("팀장"),
    TEAM_MEMBER("팀원"),
    OTHER_TEAM_APPLY("다른팀 신청자"),
    TEAM_APPLY("신청자"),
    OTHER_TEAM_MEMBER("다른팀 팀원");

    private final String role;

    Role(String role) {
        this.role = role;
    }
}
