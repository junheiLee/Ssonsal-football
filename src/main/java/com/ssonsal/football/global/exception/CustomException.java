package com.ssonsal.football.global.exception;

import com.ssonsal.football.global.util.ResponseCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 정책 상 필요한 예외 처리 시 사용
 */
@Getter
@AllArgsConstructor
public class CustomException extends RuntimeException {

    private final ResponseCode errorCode;
    private final Object data;

    public CustomException(String message, ResponseCode errorCode) {
        super(message);
        this.errorCode = errorCode;
        this.data = null;
    }

    public CustomException(Throwable cause, ResponseCode errorCode) {
        super(cause);
        this.errorCode = errorCode;
        this.data = null;
    }

    public CustomException(ResponseCode errorCode) {
        this.errorCode = errorCode;
        this.data = null;
    }
}
