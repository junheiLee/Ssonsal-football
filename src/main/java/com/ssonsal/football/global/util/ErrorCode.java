package com.ssonsal.football.global.util;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode implements ResponseCode {

    WRONG_JSON_FORMAT(HttpStatus.BAD_REQUEST, "JSON에 지원하지 않는 키워드가 있습니다."),
    TEAM_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 팀입니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 회원입니다."),
    NOT_EXIST(HttpStatus.NOT_FOUND, "존재하지 않는 내역입니다."),
    NOT_PERMISSION(HttpStatus.FORBIDDEN, "권한이 없습니다"),
    FORBIDDEN_USER(HttpStatus.FORBIDDEN, "해당 기능에 권한이 없는 사용자입니다."),
    WRONG_PASSWORD(HttpStatus.BAD_REQUEST, "잘못된 비밀번호입니다."),

    AMAZONS3_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Amazon S3 이미지 업로드에 실패하였습니다.");

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
