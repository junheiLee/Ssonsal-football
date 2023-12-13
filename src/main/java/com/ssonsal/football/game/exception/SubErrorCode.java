package com.ssonsal.football.game.exception;

import com.ssonsal.football.global.util.ResponseCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum SubErrorCode implements ResponseCode {

    /* 신청 관련 */
    NOT_REQUIRED_SUB(HttpStatus.BAD_REQUEST, "용병을 구하지 않는 매치 팀 입니다."),
    ALREADY_IN_TEAM(HttpStatus.BAD_REQUEST, "해당 팀에 소속된 회원입니다."),
    ALREADY_APPLICANT_SUB(HttpStatus.BAD_REQUEST, "이미 신청한 회원입니다."),

    /* 거절 관련 */
    NOT_EXIST_SUB_APPLICANT(HttpStatus.BAD_REQUEST, "존재하지 않는 용병 신청입니다."),

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
