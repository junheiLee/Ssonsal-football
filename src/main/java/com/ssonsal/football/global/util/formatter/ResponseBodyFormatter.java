package com.ssonsal.football.global.util.formatter;

import com.ssonsal.football.global.util.ResponseCode;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.springframework.http.ResponseEntity;

/**
 * ResponseEntity<> body에 팀 내에서 약속한 포맷으로 기입하기 위한 클래스
 * <p>
 * put(ResponseCode responseCode) 사용 시
 * {
 * "httpStatus": int,
 * "code": "string",
 * "message": "string"
 * }
 *
 * @Author junheiLee
 */
@Getter
@ToString
@SuperBuilder
public class ResponseBodyFormatter {

    private final int httpStatus;
    private final String code;
    private final String message;

    @Builder
    public ResponseBodyFormatter(int httpStatus, String code, String message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }

    /**
     * @param responseCode ResponseCode를 implements 한 enum
     * @return 약속된 포맷으로 body가 기입된 ResponseEntity
     */
    public static ResponseEntity<ResponseBodyFormatter> put(ResponseCode responseCode) {

        return ResponseEntity
                .status(responseCode.getHttpStatus())
                .body(ResponseBodyFormatter.builder()
                        .httpStatus(responseCode.getHttpStatus().value())
                        .code(responseCode.getCode())
                        .message(responseCode.getMessage())
                        .build()
                );
    }
}
