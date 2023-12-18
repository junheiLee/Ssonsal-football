package com.ssonsal.football.global.util;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum SuccessCode implements ResponseCode {

    SUCCESS(HttpStatus.OK, "요청이 성공하였습니다.");


    private final HttpStatus httpStatus;
    private final String message;

    SuccessCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

    public String getCode() {
        return name();
    }

}
