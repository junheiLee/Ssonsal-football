package com.ssonsal.football.admin.controller;

import com.ssonsal.football.admin.dto.request.AlarmDTO;
import com.ssonsal.football.admin.dto.request.EmailDTO;
import com.ssonsal.football.admin.dto.request.MessageDTO;
import com.ssonsal.football.admin.dto.response.ResponseMessageDTO;
import com.ssonsal.football.admin.service.CredentialService;
import com.ssonsal.football.admin.service.GameNotFoundException;
import com.ssonsal.football.admin.service.AlarmService;
import com.ssonsal.football.admin.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/admin")
public class MessageController {
    private final UserService userService;
    private final AlarmService alarmService;

    private final CredentialService credentialService;

    private ResponseStatusException getResponseStatusException(SnsResponse response) {
        return new ResponseStatusException(
                HttpStatus.INTERNAL_SERVER_ERROR, response.sdkHttpResponse().statusText().get()
        );
    }

    @PostMapping("/subscribeMessage")
    public ResponseEntity<String> subscribe(@RequestParam final String topicArn) {
        log.info("메세지");
        Long userId=2L;
        String userPhone = alarmService.getUserByPhone(userId);
        String memberPhone = userPhone;


        SnsClient snsClient = credentialService.getSnsClient();

            final SubscribeRequest subscribeRequest = SubscribeRequest.builder()
                    .protocol("SMS")  // SMS 프로토콜
                    .topicArn(topicArn)
                    .endpoint(memberPhone)
                    .build();

            final SubscribeResponse subscribeResponse = snsClient.subscribe(subscribeRequest);

            log.info("회원들 번호"+memberPhone);
            if (!subscribeResponse.sdkHttpResponse().isSuccessful()) {
                throw getResponseStatusException(subscribeResponse);
            }
            log.info("topicARN to subscribe = " + subscribeResponse.subscriptionArn());
            log.info("subscription list = " + snsClient.listSubscriptions());


        snsClient.close();
        return new ResponseEntity<>("TOPIC SUBSCRIBE SUCCESS", HttpStatus.OK);
    }


    @PostMapping("/publishMessage")
    public String publish(@RequestParam String topicArn, @RequestBody ResponseMessageDTO responseMessageDTO) {
        Long confirmedGameId = responseMessageDTO.getConfirmedGameId();

        try {

            MessageDTO gameInfo = alarmService.getGameInfo(confirmedGameId);

            // 메시지 작성
            String gameMessage = alarmService.buildConfirmationMessage(gameInfo);

            // SNS 클라이언트 및 전송
            SnsClient snsClient = credentialService.getSnsClient();
            PublishRequest publishRequest = PublishRequest.builder()
                    .topicArn(topicArn)
                    .message(gameMessage)
                    .build();
         //   snsClient.publish(publishRequest);

            PublishResponse publishResponse = snsClient.publish(publishRequest);
            log.info("message status:" + publishResponse.sdkHttpResponse().statusCode());

            snsClient.close();

            return "sent MSG ID = " + publishResponse.messageId();

        } catch (Exception e) {
            log.error("Error publishing message", e);
            return "Error publishing message";
        } catch (GameNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    // sms 구독 취소
    @DeleteMapping("/unsubscribeMessage")
    public ResponseEntity<String> unsubscribe(@RequestParam String topicArn) {
        log.info("주제 토픽"+ topicArn);
        Long userId = 2L;  // 유저 ID를 어떻게 가져올지에 따라 수정 필요
        String userByPhone = alarmService.getUserByPhone(userId);
        String userPhoneNumber = userByPhone.replaceAll("-", "");

        log.info("유저 핸드폰" + userByPhone);

        SnsClient snsClient = credentialService.getSnsClient();

        // ListSubscriptionsByTopic으로 Subscription 정보 가져오기
        ListSubscriptionsByTopicRequest listRequest = ListSubscriptionsByTopicRequest.builder()
                .topicArn(topicArn)
                .build();

        ListSubscriptionsByTopicResponse listResponse = snsClient.listSubscriptionsByTopic(listRequest);

        for (Subscription subscription : listResponse.subscriptions()) {
            String subscriptionEndpointWithPlus  = subscription.endpoint();

            // + 제외한 값 추출
            String subscriptionEndpoint = subscriptionEndpointWithPlus.replace("+", "");

            log.info("구독 엔드포인트 핸드폰"+subscriptionEndpoint);

            // 이메일과 endpoint 일치 여부 확인
            if (userPhoneNumber.equals(subscriptionEndpoint)) {
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
