package com.ssonsal.football.admin.controller;

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

import java.util.Map;

import static com.ssonsal.football.admin.util.AdminConstant.*;
import static com.ssonsal.football.game.util.Transfer.objectToMap;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "Email", description = "Email API")
public class SnsController {

    private final AlarmService alarmService;
    private final UserManagementService userManagementService;

    /**
     * 주제 생성
     * aws sns관리를 위한 topic 생성
     * SsonsalEmail 과 SsonsalSMS를 만들 예정이며
     * 이 부분은 관리자 페이지에서 보여지지 않고 단 2개만 생성해 쓸 것이다
     *
     * @param topicName topicName은 주제 이름이 된다
     * @return 토픽을 생성하며 성공 메시지 반환
     */

    @PostMapping("/createTopic")
    public ResponseEntity<ResponseBodyFormatter> createTopic(@RequestParam final String topicName) {


        if (topicName == null) {
            throw new CustomException(AdminErrorCode.TOPIC_CREATE_FAILED);
        }

        alarmService.createTopic(topicName);
        return ResponseBodyFormatter.put(AdminSuccessCode.TOPIC_CREATE_SUCCESS);
    }

    /**
     * 이메일 구독
     * 이메일을 수신을 원하는 사용자에게 이메일 서비스를 제공한다
     * 생성된 주제에 회원들을 구독하면 인증된 회원들에게 이메일을 보낼 수 있다
     * 구독을 실행하면 AWS에서 구독 인증 이메일이 발송된다
     *
     * @return 주제와 회원아이디를 보낸다
     */
    @PostMapping("/memberSubscribe")
    public ResponseEntity<ResponseBodyFormatter> subscribeAllMembers(@CurrentUser Account account) {

        log.info("컨트 시작");

        Long userId = account.getId();

        if (userManagementService.isAdmin(userId)) {
            throw new CustomException(AdminErrorCode.ADMIN_AUTH_FAILED);
        }

        try {
            alarmService.subscribeEmail(SSONSAL_EMAIL, userId);
            return DataResponseBodyFormatter.put(AdminSuccessCode.SUBSCRIBE_CREATE_SUCCESS, objectToMap(SUBSCRIBE_EMAIL, alarmService.subscribeEmail(SSONSAL_EMAIL, userId)));
        } catch (CustomException e) {
            log.error("이메일 구독 에러", e);
            throw new CustomException(AdminErrorCode.SUBSCRIBE_CREATE_FAILED);
        }
    }

    /**
     * 구독 인증 확인
     * 구독 인증메일이 발송되면 사용자들은 구독 인증을 확인해야한다
     * 구독 인증이 완료된것이 확인되면 요청이 완료된다
     *
     * @return 주제와 회원 email 정보와 로그인한 유저를 담아 보낸다
     */
    @PostMapping("/confirm-subscription")
    public ResponseEntity<ResponseBodyFormatter> confirmSubscription(@CurrentUser Account account) {

        Long userId = account.getId();

        if (userManagementService.isAdmin(userId)) {
            throw new CustomException(AdminErrorCode.ADMIN_AUTH_FAILED);
        }

        try {
            return DataResponseBodyFormatter.put(SuccessCode.SUCCESS, objectToMap(CONFIRM_SUBSCRIPTION, alarmService.confirmSubscription(SSONSAL_EMAIL, userId)));
        } catch (CustomException e) {
            log.error("구독 확인 실패", e);
            throw new CustomException(AdminErrorCode.SUBSCRIBE_CHECK_FAILED);
        }
    }

    /**
     * 이메일 전송 메서드
     * 구독된 사용자들에게 실제 이메일을 보내는 기능이다
     * 구독이 안된 사용자는 이메일이 전송이 안된다
     * 관리자가 작성한 text 내용이 이메일로 보내진다
     *
     * @param emailText 생성된 주제로 보내야 한다
     *                  관리자가 작성한 text(이메일에 보내질 내용)
     * @return 해당 주제로 본낸다
     */

    @PostMapping("/publishEmail")
    public ResponseEntity<ResponseBodyFormatter> publish(@RequestBody Map<String, String> emailText, @CurrentUser Account account) {

        log.info("컨트롤러 시작");

        Long userId = account.getId();

        if (userManagementService.isAdmin(userId)) {
            throw new CustomException(AdminErrorCode.ADMIN_AUTH_FAILED);
        }

        try {
            return DataResponseBodyFormatter.put(SuccessCode.SUCCESS, objectToMap(PUBLISH_EMAIL, alarmService.publishEmail(SSONSAL_EMAIL, emailText)));
        } catch (CustomException e) {
            log.error("이메일 전송 실패", e);
            throw new CustomException(AdminErrorCode.EMAIL_SEND_FAILED);
        }
    }

    /**
     * email 구독 취소
     * 이메일 전송을 원하지 않는 사용자들은 취소를 통해
     * 더이상 이메일을 안 받을 수 있다
     *
     * @return 주제 통해 구독된 이메일인지 찾고 삭제 시킨다
     */
    @DeleteMapping("/unsubscribe")
    public ResponseEntity<ResponseBodyFormatter> unsubscribe(@CurrentUser Account account) {

        Long userId = account.getId();

        if (userManagementService.isAdmin(userId)) {
            throw new CustomException(AdminErrorCode.ADMIN_AUTH_FAILED);
        }

        try {
            return DataResponseBodyFormatter.put(SuccessCode.SUCCESS, objectToMap(UNSUBSCRIBE, alarmService.unsubscribe(SSONSAL_EMAIL, userId)));
        } catch (CustomException e) {
            log.error("구독 취소 에러", e);
            throw new CustomException(AdminErrorCode.SUBSCRIBE_CANCEL_FAILED);
        }
    }

}



