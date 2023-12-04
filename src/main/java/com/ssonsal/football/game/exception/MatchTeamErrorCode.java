package com.ssonsal.football.game.exception;

import com.ssonsal.football.global.util.ResponseCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum MatchTeamErrorCode implements ResponseCode {

    ALREADY_CONFIRMED(HttpStatus.BAD_REQUEST, "해당 게임에 이미 확정된 팀입니다."),
    ALREADY_APPLICANT(HttpStatus.BAD_REQUEST, "해당 게임에 이미 신청한 팀입니다.");


    private final HttpStatus httpStatus;
    private final String message;

    MatchTeamErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

    public String getCode() {
        return name();
    }

}
