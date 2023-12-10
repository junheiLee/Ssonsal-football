package com.ssonsal.football.game.exception;

import com.ssonsal.football.global.util.ResponseCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum SubErrorCode implements ResponseCode {

    CLOSED(HttpStatus.BAD_REQUEST, "용병 신청에 실패했습니다");


    private final HttpStatus httpStatus;
    private final String message;

    SubErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

    public String getCode() {
        return name();
    }

}
