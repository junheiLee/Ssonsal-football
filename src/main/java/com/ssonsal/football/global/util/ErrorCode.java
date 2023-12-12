package com.ssonsal.football.global.util;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode implements ResponseCode {

    WRONG_JSON_FORMAT(HttpStatus.BAD_REQUEST, "JSON에 지원하지 않는 키워드가 있습니다."),
    USER_NOT_FOUND(HttpStatus.BAD_REQUEST, "존재하지 않는 회원입니다."),
    NOT_EXIST(HttpStatus.BAD_REQUEST, "존재하지 않는 내역입니다."),
    NOT_PERMISSION(HttpStatus.BAD_REQUEST, "권한이 없습니다"),
    FORBIDDEN_USER(HttpStatus.FORBIDDEN, "해당 기능에 권한이 없는 사용자입니다.");

    private final HttpStatus httpStatus;
    private final String message;

    ErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

    public String getCode() {
        return name();
    }

}
