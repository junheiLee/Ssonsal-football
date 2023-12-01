package com.ssonsal.football.game.entity;

import lombok.Getter;

@Getter
public enum MatchStatus {
    WAITING(0, "대기"),
    MATCHING(1, "확정"),
    END(2, "종료");

    private final int codeNumber;
    private final String description;

    MatchStatus(int codeNumber, String description) {
        this.codeNumber = codeNumber;
        this.description = description;
    }
}
