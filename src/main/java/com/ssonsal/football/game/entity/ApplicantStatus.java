package com.ssonsal.football.game.entity;

import lombok.Getter;

@Getter
public enum ApplicantStatus {

    WAITING("대기"),
    APPROVAL("승인"),
    REFUSAL("거절"),
    SUSPENSION("보류");

    private final String description;

    ApplicantStatus(String description) {
        this.description = description;
    }
}
