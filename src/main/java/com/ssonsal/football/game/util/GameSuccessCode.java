package com.ssonsal.football.game.util;

import com.ssonsal.football.global.util.ResponseCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum GameSuccessCode implements ResponseCode {

    WAIT_FOR_ANOTHER_TEAM(HttpStatus.ACCEPTED, "상대 팀이 기입할 때까지 기다려 주세요.");


    private final HttpStatus httpStatus;
    private final String message;

    GameSuccessCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

    public String getCode() {
        return name();
    }

}
