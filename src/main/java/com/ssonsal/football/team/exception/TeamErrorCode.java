package com.ssonsal.football.team.exception;

import com.ssonsal.football.global.util.ResponseCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * team에 관련된 요청을 할 시, 정책상 예외로 지정해야하는 코드 모음
 */
@Getter
public enum TeamErrorCode implements ResponseCode {

    MEMBER_NOT_LEADER(HttpStatus.FORBIDDEN, "팀장이 아닙니다."),
    USER_NOT_AUTHENTICATION(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다."),
    MEMBER_ALREADY_TEAM(HttpStatus.CONFLICT, "이미 팀이 있습니다."),
    LEADER_IN_GROUP(HttpStatus.BAD_REQUEST, "팀장은 탈퇴할 수 없습니다."),
    CANNOT_REMOVE_LEADER(HttpStatus.BAD_REQUEST, "팀장은 밴할 수 없습니다."),
    USER_ALREADY_APPLY(HttpStatus.CONFLICT, "신청 중인 팀이 있습니다."),
    USER_NOT_TEAM(HttpStatus.NOT_FOUND, "소속 팀이 없습니다."),
    USER_NOT_APPLY(HttpStatus.NOT_FOUND, "신청 정보가 없습니다."),
    USER_OTHER_TEAM(HttpStatus.NOT_FOUND, "다른 팀 소속입니다."),
    USER_TEAM_BANNED(HttpStatus.FORBIDDEN, "밴 또는 거절된 팀입니다"),
    USER_NOT_MEMBER(HttpStatus.NOT_FOUND, "팀원이 아닙니다."),
    DUPLICATE_TEAM_NAME(HttpStatus.CONFLICT, "팀 이름이 중복입니다.")
    ;


    private final HttpStatus httpStatus;
    private final String message;

    TeamErrorCode(HttpStatus httpStatus, String message){
        this.httpStatus = httpStatus;
        this.message = message;
    }

    public String getCode(){
        return name();
    }
}
