package com.ssonsal.football.admin.controller;

import com.ssonsal.football.admin.dto.request.ResponseMessageDTO;
import com.ssonsal.football.admin.exception.AdminErrorCode;
import com.ssonsal.football.admin.exception.AdminSuccessCode;
import com.ssonsal.football.admin.service.AlarmService;
import com.ssonsal.football.admin.service.UserManagementService;
import com.ssonsal.football.global.exception.CustomException;
import com.ssonsal.football.global.util.SuccessCode;
import com.ssonsal.football.global.util.formatter.DataResponseBodyFormatter;
import com.ssonsal.football.global.util.formatter.ResponseBodyFormatter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/api")
@Tag(name = "Message", description = "Message API")
public class MessageController {
    private final AlarmService alarmService;
    private final UserManagementService userService;

    @PostMapping("/subscribeMessage")
    public ResponseEntity<ResponseBodyFormatter> subscribe() {

        String topicArn = "arn:aws:sns:ap-northeast-1:047191174675:SsonsalMessage";

        Long userId = 2L;

        if (userService.isAdmin(userId)) {
            throw new CustomException(AdminErrorCode.USER_NOT_AUTHENTICATION);
        }

        try {
            return DataResponseBodyFormatter.put(AdminSuccessCode.SUBSCRIBE_CREATE_SUCCESS, alarmService.subscribeMessage(topicArn, userId));
        } catch (CustomException e) {
            log.error("구독 생성 실패", e);
            return DataResponseBodyFormatter.put(AdminErrorCode.SUBSCRIBE_CREATE_FAILED);
        }
    }

    @PostMapping("/publishMessage")
    public ResponseEntity<ResponseBodyFormatter> publish(@RequestBody ResponseMessageDTO responseMessageDTO) {

        String topicArn = "arn:aws:sns:ap-northeast-1:047191174675:SsonsalMessage";

        Long userId = 2L;

        if (userService.isAdmin(userId)) {
            throw new CustomException(AdminErrorCode.USER_NOT_AUTHENTICATION);
        }

        try {
            return DataResponseBodyFormatter.put(SuccessCode.SUCCESS, alarmService.publishMessage(topicArn, responseMessageDTO));
        } catch (CustomException e) {
            log.error("메세지 전송 실패", e);
            return DataResponseBodyFormatter.put(AdminErrorCode.MESSAGE_SEND_FAILED);
        }
    }

    // sms 구독 취소
    @DeleteMapping("/unsubscribeMessage")
    public ResponseEntity<ResponseBodyFormatter> unsubscribe() {

        String topicArn = "arn:aws:sns:ap-northeast-1:047191174675:SsonsalMessage";

        Long userId = 2L;

        if (userService.isAdmin(userId)) {
            throw new CustomException(AdminErrorCode.USER_NOT_AUTHENTICATION);
        }
        try {
            alarmService.unsubscribeMessage(topicArn, userId);
            return ResponseBodyFormatter.put(SuccessCode.SUCCESS);
        } catch (CustomException e) {
            log.error("메세지 구독 취소 실패", e);
            return DataResponseBodyFormatter.put(AdminErrorCode.SUBSCRIBE_CANCEL_FAILED);
        }
    }


}
