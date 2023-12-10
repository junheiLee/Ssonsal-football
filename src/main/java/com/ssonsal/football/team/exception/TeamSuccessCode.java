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
    USER_TEAM_APPLY(HttpStatus.CREATED, "신청이 완료되었습니다."),
    USER_APPLY_CANCEL(HttpStatus.CREATED, "신청이 취소되었습니다."),
    USER_TEAM_LEAVE(HttpStatus.OK, "탈퇴가 완료되었습니다."),
    LEADER_APPLY_ACCEPT(HttpStatus.OK, "수락이 완료되었습니다."),
    LEADER_APPLY_REJECT(HttpStatus.OK, "거절이 완료되었습니다."),
    LEADER_DELEGATE_SUCCESS(HttpStatus.OK, "위임이 완료되었습니다."),
    LEADER_MEMBER_BANNED(HttpStatus.OK, "밴을 성공하였습니다."),
    LEADER_EDIT_SUCCESS(HttpStatus.OK, "팀 정보 수정이 완료되었습니다."),
    LEADER_REJECT_CANCEL(HttpStatus.OK, "취소가 완료되었습니다.")
    ;

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
