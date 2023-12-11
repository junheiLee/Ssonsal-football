package com.ssonsal.football.global.util;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode implements ResponseCode {

    USER_NOT_FOUND(HttpStatus.BAD_REQUEST, "사용 불가한 유저입니다."),
    NOT_EXIST(HttpStatus.BAD_REQUEST, "존재하지 않는 내역입니다."),
    NOT_PERMISSION(HttpStatus.BAD_REQUEST,"권한이 없습니다");
    FORBIDDEN_USER(HttpStatus.FORBIDDEN, "해당 기능에 권한이 없는 사용자입니다."),
    WRONG_FORMAT(HttpStatus.BAD_REQUEST, "올바르지 않는 형식입니다."),

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
