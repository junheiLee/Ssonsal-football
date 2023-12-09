package com.ssonsal.football.game.exception;

import com.ssonsal.football.global.util.ResponseCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum MatchErrorCode implements ResponseCode {

    ALREADY_APPROVAL_TEAM(HttpStatus.BAD_REQUEST, "해당 게임에 이미 승인된 팀입니다."),
    ALREADY_APPLICANT_TEAM(HttpStatus.BAD_REQUEST, "해당 게임에 이미 신청한 팀입니다."),
    NOT_APPLICANT_TEAM(HttpStatus.BAD_REQUEST, "신청하지 않은 팀입니다."),
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
