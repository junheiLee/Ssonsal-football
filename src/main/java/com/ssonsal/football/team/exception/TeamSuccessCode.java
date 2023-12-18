package com.ssonsal.football.team.exception;

import com.ssonsal.football.global.util.ResponseCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * team에 관련된 요청을 할 시, 성공 시 응답을 주기위한 코드 모음
 */
@Getter
public enum TeamSuccessCode implements ResponseCode {

    USER_TEAM_CREATED(HttpStatus.CREATED, "팀이 생성되었습니다."),
    USER_TEAM_APPLY(HttpStatus.CREATED, "신청이 완료되었습니다.");

    private final HttpStatus httpStatus;
    private final String message;

    TeamSuccessCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

    public String getCode() {
        return name();
    }
}
