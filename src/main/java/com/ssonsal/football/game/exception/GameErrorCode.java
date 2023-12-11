package com.ssonsal.football.game.exception;

import com.ssonsal.football.global.util.ResponseCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * game matching(상대 팀, 용병) 시, 정책상 예외로 지정해야하는 코드 모음
 */
@Getter
public enum GameErrorCode implements ResponseCode {

    /* 생성 관련 */
    NOT_EXIST_GAME(HttpStatus.BAD_REQUEST, "존재하지 않는 게임입니다."),
    NOT_FOUND_TARGET(HttpStatus.BAD_REQUEST, "구인 대상이 없어 게임을 생성할 수 없습니다."),
    NOT_IN_TEAM(HttpStatus.FORBIDDEN, "팀이 없는 회원은 게임을 생성할 수 없습니다."),

    /* 게임 결과 입력 관련 */
    IMPOSSIBLE_RESULT(HttpStatus.BAD_REQUEST, "기입할 수 없는 게임 결과 형식입니다."),
    CAN_NOT_ENTER_RESULT(HttpStatus.BAD_REQUEST, "결과를 기입할 수 없는 게임입니다."),

    /* 팀 권한 관련 */
    NOT_IN_TARGET_TEAM(HttpStatus.FORBIDDEN, "해당 팀에 대한 권한이 없는 회원입니다."),

    /* 신청 관련 */
    ALREADY_APPROVAL_TEAM(HttpStatus.BAD_REQUEST, "해당 게임에 이미 승인된 팀입니다."),
    ALREADY_APPLICANT_TEAM(HttpStatus.BAD_REQUEST, "해당 게임에 이미 신청한 팀입니다."),
    NOT_EXIST_APPLICATION(HttpStatus.BAD_REQUEST, "해당하는 신청이 없습니다."),
    NOT_WAITING_GAME(HttpStatus.BAD_REQUEST, "대기 중인 게임이 아닙니다."),

    /* 팀 관련 */
    NOT_EXIST_TEAM(HttpStatus.BAD_REQUEST, "존재하지 않는 팀입니다.");

    private final HttpStatus httpStatus;
    private final String message;

    GameErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

    public String getCode() {
        return name();
    }

}
