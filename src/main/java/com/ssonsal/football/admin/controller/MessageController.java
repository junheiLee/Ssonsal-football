package com.ssonsal.football.admin.controller;

import com.ssonsal.football.admin.dto.response.ResponseMessageDTO;
import com.ssonsal.football.admin.exception.AdminErrorCode;
import com.ssonsal.football.admin.exception.AdminSuccessCode;
import com.ssonsal.football.admin.service.AlarmService;
import com.ssonsal.football.global.exception.CustomException;
import com.ssonsal.football.global.util.formatter.DataResponseBodyFormatter;
import com.ssonsal.football.global.util.formatter.ResponseBodyFormatter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/admin")
public class MessageController {
    private final AlarmService alarmService;

    @PostMapping("/subscribeMessage")
    public ResponseEntity<ResponseBodyFormatter> subscribe(@RequestParam final String topicArn) {
        Long userId = 2L;

        if (userId == null) {
            throw new CustomException(AdminErrorCode.USER_NOT_AUTHENTICATION);
        }

        try {
            return DataResponseBodyFormatter.put(AdminSuccessCode.SUBSCRIBE_CREATE_SUCCESS,alarmService.subscribeMessage(topicArn, userId));
        } catch (CustomException e) {
            log.error("구독 생성 실패", e);
            return DataResponseBodyFormatter.put(AdminErrorCode.SUBSCRIBE_CREATE_FAILED);
        }
    }

    @PostMapping("/publishMessage")
    public ResponseEntity<ResponseBodyFormatter> publish(@RequestParam String topicArn, @RequestBody ResponseMessageDTO responseMessageDTO) {
        Long userId= 2L;

        if (userId == null) {
            throw new CustomException(AdminErrorCode.USER_NOT_AUTHENTICATION);
        }

        try {
            return DataResponseBodyFormatter.put(AdminSuccessCode.MESSAGE_SEND_SUCCESS,alarmService.publishMessage(topicArn, responseMessageDTO));
        } catch (Exception e) {
            log.error("메세지 전송 실패", e);
            return DataResponseBodyFormatter.put(AdminErrorCode.MESSAGE_SEND_FAILED);
        }
    }

    // sms 구독 취소
    @DeleteMapping("/unsubscribeMessage")
    public ResponseEntity<ResponseBodyFormatter> unsubscribe(@RequestParam String topicArn) {
        Long userId= 2L;

        if (userId == null) {
            throw new CustomException(AdminErrorCode.USER_NOT_AUTHENTICATION);
        }
        try {
            alarmService.unsubscribeMessage(topicArn,userId);
            return ResponseBodyFormatter.put(AdminSuccessCode.SUBSCRIBE_CANCEL_SUCCESS);
        } catch (Exception e) {
            log.error("메세지 구독 취소 실패", e);
            return DataResponseBodyFormatter.put(AdminErrorCode.SUBSCRIBE_CANCEL_FAILED);
        }
        }


}
