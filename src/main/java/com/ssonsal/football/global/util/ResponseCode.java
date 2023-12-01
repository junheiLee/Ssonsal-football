package com.ssonsal.football.global.util;

import org.springframework.http.HttpStatus;

/**
 * ResponseBody에 들어가야 할 필수 코드를 강제하는 인터페이스
 * SuccessCode와 ErrorCode를 이넘으로 작성 시, implements해야 한다.
 *
 * com.ssonsal.football.global.util.formatter.ResponseBodyFormatter.put() 메서드 파라미터 타입
 */
public interface ResponseCode {

    HttpStatus getHttpStatus();

    String getCode();

    String getMessage();

}
