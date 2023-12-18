package com.ssonsal.football.admin.exception;

import com.ssonsal.football.global.util.ResponseCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum AdminSuccessCode implements ResponseCode {


    TOPIC_CREATE_SUCCESS(HttpStatus.CREATED, "주제 생성 성공."),
    SUBSCRIBE_CREATE_SUCCESS(HttpStatus.CREATED, "구독 생성 성공."),
    CONTENT_WRITING_SUCCESS(HttpStatus.CREATED, "내용 작성 성공.");

    private final HttpStatus httpStatus;
    private final String message;


    AdminSuccessCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

    public String getCode() {
        return name();
    }
}
