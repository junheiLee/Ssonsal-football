package com.ssonsal.football.admin.controller;

import com.ssonsal.football.admin.dto.request.ResponseMessageDTO;
import com.ssonsal.football.admin.exception.AdminErrorCode;
import com.ssonsal.football.admin.exception.AdminSuccessCode;
import com.ssonsal.football.admin.service.AlarmService;
import com.ssonsal.football.admin.service.UserManagementService;
import com.ssonsal.football.global.account.Account;
import com.ssonsal.football.global.account.CurrentUser;
import com.ssonsal.football.global.exception.CustomException;
import com.ssonsal.football.global.util.SuccessCode;
import com.ssonsal.football.global.util.formatter.DataResponseBodyFormatter;
import com.ssonsal.football.global.util.formatter.ResponseBodyFormatter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.ssonsal.football.admin.util.AdminConstant.*;
import static com.ssonsal.football.game.util.Transfer.objectToMap;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/api")
@Tag(name = "Message", description = "Message API")
public class MessageController {

    private final AlarmService alarmService;
    private final UserManagementService userManagementService;

    @PostMapping("/subscribeMessage")
    public ResponseEntity<ResponseBodyFormatter> subscribe(@CurrentUser Account account) {

        Long userId = account.getId();


        if (userManagementService.isAdmin(userId)) {
            throw new CustomException(AdminErrorCode.ADMIN_AUTH_FAILED);

        }
        alarmService.updateUserRole(userId);
        try {
            return DataResponseBodyFormatter.put(AdminSuccessCode.SUBSCRIBE_CREATE_SUCCESS, objectToMap(SUBSCRIBE_MESSAGE, alarmService.subscribeMessage(SSONSAL_MESSAGE, userId)));
        } catch (CustomException e) {
            log.error("구독 생성 실패", e);
            throw new CustomException(AdminErrorCode.SUBSCRIBE_CREATE_FAILED);
        }
    }

    @PostMapping("/publishMessage")
    public ResponseEntity<ResponseBodyFormatter> publish(@RequestBody ResponseMessageDTO responseMessageDTO, @CurrentUser Account account) {

        Long userId = account.getId();

        if (userManagementService.isAdmin(userId)) {
            throw new CustomException(AdminErrorCode.ADMIN_AUTH_FAILED);

        }

        try {
            return DataResponseBodyFormatter.put(SuccessCode.SUCCESS, objectToMap(PUBLISH_MESSAGE, alarmService.publishMessage(SSONSAL_MESSAGE, responseMessageDTO)));
        } catch (CustomException e) {
            log.error("메세지 전송 실패", e);
            throw new CustomException(AdminErrorCode.MESSAGE_SEND_FAILED);
        }
    }

    // sms 구독 취소
    @DeleteMapping("/unsubscribeMessage")
    public ResponseEntity<ResponseBodyFormatter> unsubscribe(@CurrentUser Account account) {

        Long userId = account.getId();

        if (userManagementService.isAdmin(userId)) {
            throw new CustomException(AdminErrorCode.ADMIN_AUTH_FAILED);
        }

        try {

            alarmService.updateUserRole(userId);
            alarmService.unsubscribeMessage(SSONSAL_MESSAGE, userId);

            return ResponseBodyFormatter.put(SuccessCode.SUCCESS);
        } catch (CustomException e) {
            log.error("메세지 구독 취소 실패", e);
            throw new CustomException(AdminErrorCode.SUBSCRIBE_CANCEL_FAILED);
        }
    }

}
