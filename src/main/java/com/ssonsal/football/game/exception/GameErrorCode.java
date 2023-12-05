package com.ssonsal.football.game.exception;

import com.ssonsal.football.global.util.ResponseCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * game matching(상대 팀, 용병) 시, 정책상 예외로 지정해야하는 코드 모음
 */
@Getter
public enum GameErrorCode implements ResponseCode {

    WRITER_NOT_IN_TEAM(HttpStatus.BAD_REQUEST, "팀이 없는 회원은 매칭을 잡을 수 없습니다."),
    NOT_FOUND_TARGET(HttpStatus.BAD_REQUEST, "구인 대상을 찾을 수 없습니다.(상대 팀 혹은 용병을 구하는 기능입니다.)"),
    ALREADY_CONFIRMED_GAME(HttpStatus.BAD_REQUEST, "이미 확정된 게임입니다."),
    CAN_NOT_ENTER_RESULT(HttpStatus.BAD_REQUEST, "결과를 기입할 수 없는 게임입니다.");


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
