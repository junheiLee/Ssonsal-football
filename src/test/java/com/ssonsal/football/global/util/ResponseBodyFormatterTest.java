package com.ssonsal.football.global.util;

import com.ssonsal.football.global.util.formatter.DataResponseBodyFormatter;
import com.ssonsal.football.global.util.formatter.ResponseBodyFormatter;
import lombok.ToString;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
public class ResponseBodyFormatterTest {

    @Test
    void 데이터가_없을_때_포맷에_맞게_엔티티로_변환하는지_테스트() {
        //when
        ResponseEntity<ResponseBodyFormatter> testResponse = ResponseBodyFormatter.put(SuccessCode.SUCCESS);

        //then
        assertThat(testResponse.getStatusCode()).isEqualTo(SuccessCode.SUCCESS.getHttpStatus()); //상태 코드 확인
        assertThat(Objects.requireNonNull(testResponse.getBody()).toString()).contains(SuccessCode.SUCCESS.getMessage()); //바디 메시지 확인
        assertThat(testResponse.getBody().toString()).doesNotContain("data:"); //바디에 데이터 없는 지 확인

    }

    @Test
    void 데이터가_있을_때_포맷에_맞게_엔티티로_변환하는지_테스트() {
        //given
        TestData testData = new TestData("손흥민", null);

        //when
        ResponseEntity<ResponseBodyFormatter> testResponse = DataResponseBodyFormatter.put(SuccessCode.SUCCESS, testData);

        //then
        assertThat(testResponse.getStatusCode()).isEqualTo(SuccessCode.SUCCESS.getHttpStatus()); //상태 코드 확인
        assertThat(Objects.requireNonNull(testResponse.getBody()).toString()).contains(SuccessCode.SUCCESS.getMessage()); //바디 메시지 확인
        assertThat(testResponse.getBody().toString()).contains(testData.toString()); //바디 데이터 확인

    }

    @ToString
    static class TestData {
        String name;
        String position;

        public TestData(String name, String position) {
            this.name = name;
            this.position = position;
        }
    }
}
