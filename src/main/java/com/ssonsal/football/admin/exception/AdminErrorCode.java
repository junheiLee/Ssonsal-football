package com.ssonsal.football.admin.exception;

import com.ssonsal.football.global.util.ResponseCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum AdminErrorCode implements ResponseCode {

    USER_NOT_AUTHENTICATION(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다."),
    USER_FAILED_Authorization(HttpStatus.FORBIDDEN, "권한 부여 실패."),
    USER_SELECTED_FAILED(HttpStatus.BAD_REQUEST, "선택된 유저가 없습니다."),
    USER_NOT_FOUND(HttpStatus.BAD_REQUEST, "선택된 유저가 없습니다."),
    INVALID_DATE_FORMAT(HttpStatus.BAD_REQUEST, "날짜 포맷 실패"),

    GAME_NOT_FOUND(HttpStatus.NOT_FOUND, "게임을 찾을 수 없습니다."),
    GAME_NOT_CONFIRMED(HttpStatus.BAD_REQUEST, "확정된 게임이 아닙니다."),

    MONTH_UPDATE_FAILED(HttpStatus.BAD_REQUEST, "달 변경 실패."),
    DELETEDCODE_UPDATE_FAILED(HttpStatus.NOT_FOUND, "삭제 코드 변환 실패"),
    DATE_NOT_FOUND(HttpStatus.BAD_REQUEST, "날짜를 찾을 수 없습니다"),

    TOPIC_CREATE_FAILED(HttpStatus.NOT_FOUND, "주제 생성 실패"),
    TOPIC_RESPONSE_FAILED(HttpStatus.BAD_REQUEST, "주제 응답 실패."),
    SUBSCRIBE_RESPONSE_FAILED(HttpStatus.BAD_REQUEST, "구독 응답 실패."),
    SUBSCRIBE_CREATE_FAILED(HttpStatus.BAD_REQUEST, "구독 생성 실패."),
    SUBSCRIBE_CANCEL_FAILED(HttpStatus.BAD_REQUEST, "구독 취소 실패."),
    SUBSCRIBE_CHECK_FAILED(HttpStatus.BAD_REQUEST, "구독 인증 확인 실패."),
    EMAIL_CHECK_FAILED2(HttpStatus.BAD_REQUEST, "구독 인증 확인 실패."),
    SUBSCRIPTIONARN_NOT_FOUND(HttpStatus.BAD_REQUEST, "유효하지 않은 구독ARN."),
    EMAIL_NOT_FOUND(HttpStatus.BAD_REQUEST, "유효하지 않는 이메일."),
    EMAIL_SEND_FAILED(HttpStatus.BAD_REQUEST, "이메일 전송 실패."),
    MESSAGE_SEND_FAILED(HttpStatus.BAD_REQUEST, "메시지 전송 실패."),
    CONTENT_WRITING_FAILED(HttpStatus.BAD_REQUEST, " 내용 작성 실패.");


    private final HttpStatus httpStatus;
    private final String message;

    AdminErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

    public String getCode() {
        return name();
    }
}

