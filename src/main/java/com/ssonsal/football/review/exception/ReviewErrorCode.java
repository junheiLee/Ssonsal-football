package com.ssonsal.football.review.exception;

import com.ssonsal.football.global.util.ResponseCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ReviewErrorCode implements ResponseCode {

    REVIEW_NOT_FOUND(HttpStatus.BAD_REQUEST, "리뷰가 존재하지 않습니다."),
    REPORT_NOT_FOUND(HttpStatus.BAD_REQUEST, "신고된 리뷰가 존재하지 않습니다."),
    GAME_NOT_FOUND(HttpStatus.BAD_REQUEST, "게임ID를 찾을 수 없습니다."),
    USER_NOT_FOUND(HttpStatus.BAD_REQUEST, "유저ID를 찾을 수 없습니다."),
    NO_QUALIFICATION(HttpStatus.BAD_REQUEST, "리뷰를 작성할 수 없습니다.");



    private final HttpStatus httpStatus;
    private final String message;

    ReviewErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

    public String getCode() {
        return name();
    }

}
