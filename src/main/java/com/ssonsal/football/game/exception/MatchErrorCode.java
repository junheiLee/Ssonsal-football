package com.ssonsal.football.game.exception;

import com.ssonsal.football.global.util.ResponseCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum MatchErrorCode implements ResponseCode {


    IMPOSSIBLE_RESULT(HttpStatus.BAD_REQUEST, "기입할 수 없는 게임 결과입니다."),
    NOT_TEAM_MEMBER(HttpStatus.BAD_REQUEST, "해당 팀에 대한 권한이 없는 회원입니다.");


    private final HttpStatus httpStatus;
    private final String message;

    MatchErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

    public String getCode() {
        return name();
    }

}
