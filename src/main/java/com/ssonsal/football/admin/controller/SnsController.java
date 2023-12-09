package com.ssonsal.football.admin.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssonsal.football.admin.dto.request.AlarmDTO;
import com.ssonsal.football.admin.dto.response.ResponseEmailDTO;
import com.ssonsal.football.admin.service.AlarmService;
import com.ssonsal.football.admin.service.CredentialService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.*;

import javax.servlet.http.HttpSession;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class SnsController {


    private final AlarmService alarmService;

    private final CredentialService credentialService;

    private ResponseStatusException getResponseStatusException(SnsResponse response) {
        return new ResponseStatusException(
                HttpStatus.INTERNAL_SERVER_ERROR, response.sdkHttpResponse().statusText().get()
        );
    }

    // 주제 생성
    @PostMapping("/createTopic")
    public ResponseEntity<String> createTopic(@RequestParam final String topicName) {
        final CreateTopicRequest createTopicRequest = CreateTopicRequest.builder()
                .name(topicName)
                .build();
        SnsClient snsClient = credentialService.getSnsClient();
        final CreateTopicResponse createTopicResponse = snsClient.createTopic(createTopicRequest);

        if (!createTopicResponse.sdkHttpResponse().isSuccessful()) {
            throw getResponseStatusException(createTopicResponse);
        }
        log.info("topic name = " + createTopicResponse.topicArn());
        log.info("topic list = " + snsClient.listTopics())  ;
        snsClient.close();
        return new ResponseEntity<>("TOPIC CREATING SUCCESS", HttpStatus.OK);
    }


    // 이메일 구독
    @PostMapping("/memberSubscribe")
    public ResponseEntity<AlarmDTO> subscribeAllMembers(@RequestParam final String topicArn) {

        Long userId = 2L; // 특정 유저 아이디를 지정
        String userEmail = alarmService.getUserByEmail(userId);

           log.info("유저 아이디"+userEmail);

       // String userEmail = (String) session.getAttribute("userEmail");



        SnsClient snsClient = credentialService.getSnsClient();

        SubscribeRequest subscribeRequest = SubscribeRequest.builder()
                .protocol("email")
                .topicArn(topicArn)
                .endpoint(userEmail)
                .build();

        SubscribeResponse subscribeResponse = snsClient.subscribe(subscribeRequest);

        log.info("구독 request"+subscribeRequest);
        log.info("구독 Response"+subscribeResponse);

        if (!subscribeResponse.sdkHttpResponse().isSuccessful()) {
            throw getResponseStatusException(subscribeResponse);
        }

        // DB에 정보 저장 (subscriptionArn은 아직 pending confirmation 상태로 가정)
        AlarmDTO alarmEmailInfo =alarmService.saveAlarmInfo(topicArn, "pending confirmation",userEmail);

        log.info("구독 정보"+ alarmEmailInfo);

        log.info("Subscribed member: " + userEmail);
        snsClient.close();

        return new ResponseEntity<AlarmDTO>(alarmEmailInfo, HttpStatus.OK);
    }

    @PostMapping("/confirm-subscription")
    public ResponseEntity<String> confirmSubscription(@RequestParam String topicArn, @RequestBody ResponseEmailDTO responseEmailDTO) {
        Long userId = 2L;
        String confirmSubscriptionArn = responseEmailDTO.getSubscriptionArn();
        String infoEmail = responseEmailDTO.getUserEmail();

        // 사용자 정보 가져오기
        String userByEmail = alarmService.getUserByEmail(userId);

        log.info("가져온 이메일: " + infoEmail);
        log.info("현재 유저 이메일: " + userByEmail);

        // 이메일 일치 여부 확인
        if (infoEmail == null || !infoEmail.equals(userByEmail)) {
            return new ResponseEntity<>("유효하지 않은 사용자 이메일", HttpStatus.BAD_REQUEST);
        }

        SnsClient snsClient = credentialService.getSnsClient();

        // ListSubscriptionsByTopic으로 Subscription 정보 가져오기
        ListSubscriptionsByTopicRequest listRequest = ListSubscriptionsByTopicRequest.builder()
                .topicArn(topicArn)
                .build();

        ListSubscriptionsByTopicResponse listResponse = snsClient.listSubscriptionsByTopic(listRequest);

        // Subscription 정보 확인
        for (Subscription subscription : listResponse.subscriptions()) {
            String subscriptionArn = subscription.subscriptionArn();
            String endpoint = subscription.endpoint();
            log.info(endpoint+"엔드포인트");
            log.info(subscriptionArn+"구독 arn");

            // 이메일과 endpoint 일치 여부 확인
            if (userByEmail.equals(endpoint)) {
                if ("PendingConfirmation".equals(subscriptionArn)) {
                    return new ResponseEntity<>("구독 확인이 안됬습니다. 다시 확인해 주세요", HttpStatus.NOT_FOUND);
                } else {
                    return new ResponseEntity<>("구독 확인이 되었습니다. 감사합니다", HttpStatus.OK);
                }
            }
        }

        return new ResponseEntity<>("해당 유저의 구독 정보를 찾을 수 없습니다", HttpStatus.NOT_FOUND);
    }

    // 이메일 전송 메서드
    @PostMapping("/publishEmail")
    public ResponseEntity publish(@RequestParam String topicArn,
                          @RequestParam int number
                         //,@RequestParam String text
                                  ) {
        try {
            SnsClient snsClient = credentialService.getSnsClient();

            // 하드코딩된 텍스트
            String text = "hello";

            final PublishRequest publishRequest = PublishRequest.builder()
                    .topicArn(topicArn)
                    .subject("HTTP ENDPOINT TEST MESSAGE")
                    .message(text)  // 수정된 부분
                    .build();

            log.info("메세지 내용"+text);
            // SNS 주제에 메시지 발송
             PublishResponse publishResponse = snsClient.publish(publishRequest);

            log.info("Message published to topic: " + topicArn);

            snsClient.close();
            log.info("글3"+text);
            return new ResponseEntity<>("Message published successfully. MessageId: " + publishResponse.messageId(), HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error publishing message: " + e.getMessage(), e);
            return new ResponseEntity<>("Error publishing message: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // email 구독 취소
    @DeleteMapping("/unsubscribeEmail")
    public ResponseEntity<String> unsubscribe(@RequestParam String topicArn) {
        Long userId = 2L;  // 유저 ID를 어떻게 가져올지에 따라 수정 필요
        String userByEmail = alarmService.getUserByEmail(userId);

        SnsClient snsClient = credentialService.getSnsClient();

        // ListSubscriptionsByTopic으로 Subscription 정보 가져오기
        ListSubscriptionsByTopicRequest listRequest = ListSubscriptionsByTopicRequest.builder()
                .topicArn(topicArn)
                .build();

        ListSubscriptionsByTopicResponse listResponse = snsClient.listSubscriptionsByTopic(listRequest);

        for (Subscription subscription : listResponse.subscriptions()) {
            String subscriptionEndpoint = subscription.endpoint();

            // 이메일과 endpoint 일치 여부 확인
            if (userByEmail.equals(subscriptionEndpoint)) {
                UnsubscribeRequest unsubscribeRequest = UnsubscribeRequest.builder()
                        .subscriptionArn(subscription.subscriptionArn())
                        .build();

                UnsubscribeResponse unsubscribeResponse = snsClient.unsubscribe(unsubscribeRequest);

                snsClient.close();

                if (unsubscribeResponse.sdkHttpResponse().isSuccessful()) {
                    log.info("Unsubscribed successfully: " + subscription.subscriptionArn());
                    return new ResponseEntity<>("Unsubscribed Successfully", HttpStatus.OK);
                } else {
                    throw getResponseStatusException(unsubscribeResponse);
                }
            }
        }

        // 찾지 못한 경우
        return new ResponseEntity<>("해당하는 구독을 찾을 수 없습니다", HttpStatus.NOT_FOUND);
    }

}



