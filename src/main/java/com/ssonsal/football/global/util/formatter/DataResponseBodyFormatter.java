package com.ssonsal.football.global.util.formatter;

import com.ssonsal.football.global.util.ResponseCode;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.http.ResponseEntity;

/**
 * ResponseEntity<> body에 팀 내에서 약속한 포맷으로 기입하기 위한 클래스
 *
 * put(ResponseCode responseCode, Object data) 사용 시
 * {
 *     "httpStatus": int,
 *     "code": "string",
 *     "message": "string"
 *     "data": {
 *         object
 *     }
 * }
 *
 * @Author junheiLee
 */
@Getter
@ToString(callSuper = true)
public class DataResponseBodyFormatter extends ResponseBodyFormatter {

    private final Object data;

    @Builder(builderMethodName = "dataBuilder")
    public DataResponseBodyFormatter(int httpStatus, String code, String message, Object data) {
        super(httpStatus, code, message);
        this.data = data;
    }

    /**
     * @param responseCode ResponseCode를 implements 한 enum
     * @Param data 반환해주어야 하는 ResponseDto
     * @return 약속된 포맷으로 body가 기입된 ResponseEntity
     */
    public static ResponseEntity<ResponseBodyFormatter> put(ResponseCode responseCode, Object data) {

        return ResponseEntity
                .status(responseCode.getHttpStatus())
                .body(DataResponseBodyFormatter.dataBuilder()
                        .httpStatus(responseCode.getHttpStatus().value())
                        .code(responseCode.getCode())
                        .message(responseCode.getMessage())
                        .data(data)
                        .build()
                );
    }
}
