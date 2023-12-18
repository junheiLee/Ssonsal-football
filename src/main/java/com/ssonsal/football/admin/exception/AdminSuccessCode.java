package com.ssonsal.football.admin.exception;

import com.ssonsal.football.global.util.ResponseCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum AdminSuccessCode implements ResponseCode {

    ADMIN_AUTH_SUCCESS(HttpStatus.OK, "관리자 인증 성공"),
    USER_COUNT_SUCCESS(HttpStatus.OK, "유저 카운트 성공"),
    PAGE_ALTER_SUCCESS(HttpStatus.OK, "페이지 이동 성공"),
    AMDIN_RECOGNIZE_SUCCESS(HttpStatus.OK, "관리자 승인 성공."),
    GAME_DELETE_SUCCESS(HttpStatus.OK, "게임이 삭제되었습니다."),
    GAME_DELETE_FAILED(HttpStatus.OK, "게임 글 삭제 성공."),
    GAME_POST_DELETED(HttpStatus.OK, "게시글이 삭제되었습니다"),

    MONTH_UPDATE_SUCCESS(HttpStatus.OK, "달 변경 성공."),

    TOPIC_CREATE_SUCCESS(HttpStatus.CREATED, "주제 생성 성공."),
    SUBSCRIBE_CREATE_SUCCESS(HttpStatus.CREATED, "구독 생성 성공."),
    SUBSCRIBE_CANCEL_SUCCESS(HttpStatus.OK, "구독 취소 성공."),
    SUBSCRIBE_CHECK_SUCCESS(HttpStatus.OK, "구독 확인 성공."),
    EMAIL_SEND_SUCCESS(HttpStatus.OK, "이메일 전송 선공."),
    MESSAGE_SEND_SUCCESS(HttpStatus.OK, "메시지 전송 선공."),
    CONTENT_WRITING_SUCCESS(HttpStatus.CREATED, "내용 작성 성공.");

    private final HttpStatus httpStatus;
    private final String message;


    AdminSuccessCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

    public String getCode() {
        return name();
    }
}
