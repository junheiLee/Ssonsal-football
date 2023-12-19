package com.ssonsal.football.admin.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssonsal.football.admin.dto.request.ResponseMessageDTO;
import com.ssonsal.football.admin.dto.response.AlarmDTO;
import com.ssonsal.football.admin.dto.response.MessageDTO;
import com.ssonsal.football.admin.exception.AdminErrorCode;
import com.ssonsal.football.admin.repository.GameManagementRepository;
import com.ssonsal.football.game.entity.Game;
import com.ssonsal.football.global.exception.CustomException;
import com.ssonsal.football.global.util.ErrorCode;
import com.ssonsal.football.user.entity.User;
import com.ssonsal.football.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.*;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class AlarmServiceImpl implements AlarmService {

    private final GameManagementRepository gameManagementRepository;
    private final UserRepository userRepository;
    private final CredentialServiceImpl credentialServiceImpl;

    @Override
    public MessageDTO getGameInfo(Long confirmedGameId) {
        Game game = gameManagementRepository.findById(confirmedGameId)
                .orElseThrow(() -> new CustomException(AdminErrorCode.GAME_NOT_FOUND, confirmedGameId));

        // if (game.getMatchStatus() == 1) {
        return MessageDTO.builder()
                .hometeamId(game.getHome())
                .schedule(game.getSchedule())
                .gameTime(game.getGameTime())
                .stadium(game.getStadium())
                .vsFormat(game.getVsFormat())
                .account(game.getAccount())
                .build();
        /*} else {
            throw new CustomException(AdminErrorCode.GAME_NOT_CONFIRMED, confirmedGameId);
        }*/
    }

    @Override
    public AlarmDTO saveAlarmInfo(String subscriptionArn, String userEmail) {

        return AlarmDTO.builder()
                .subscriptionArn(subscriptionArn)
                .userEmail(userEmail)
                .build();
    }

    @Override
    public String getUserByEmail(Long userId) {

        return userRepository.findById(userId)
                .map(User::getEmail)
                .orElseThrow(() -> new CustomException(AdminErrorCode.USER_NOT_FOUND, userId));
    }

    @Override
    public String getUserByPhone(Long userId) {
        return userRepository.findById(userId)
                .map(User::getPhone)
                .orElseThrow(() -> new CustomException(AdminErrorCode.USER_NOT_FOUND, userId));
    }

    @Override
    public String buildConfirmationMessage(MessageDTO gameInfo) {
        log.info("문자 데이터" + gameInfo);

        try {
            ObjectMapper objectMapper = new ObjectMapper();

            String message = gameInfo.getHometeamId().getName() + " 팀과의 매치가 확정되었습니다.\n" +
                    "매치 날짜: " + gameInfo.getSchedule() + "\n" +
                    "매치 시간: " + gameInfo.getGameTime() + " 시간\n" +
                    "경기장: " + gameInfo.getStadium() + "\n" +
                    "플레이어: " + gameInfo.getVsFormat() + "\n" +
                    "번호: " + gameInfo.getPhone() + "\n" +
                    "비용: " + gameInfo.getAccount();


            log.info("메세지 내용: {}", message);

            String jsonMessage = objectMapper.writeValueAsString(Map.of("message", message));

            log.info("JSON Message: {}", jsonMessage);

            return jsonMessage;

        } catch (Exception e) {
            log.error("처리 중 오류 발생", e);
            return "오류 발생";
        }
    }

    @Override
    public void createTopic(String topicName) {

        try (SnsClient snsClient = credentialServiceImpl.getSnsClient()) {
            final CreateTopicRequest createTopicRequest = CreateTopicRequest.builder()
                    .name(topicName)
                    .build();
            final CreateTopicResponse createTopicResponse = snsClient.createTopic(createTopicRequest);

            if (!createTopicResponse.sdkHttpResponse().isSuccessful()) {
                throw new CustomException(AdminErrorCode.TOPIC_RESPONSE_FAILED, createTopicResponse);
            }

            log.info("주제 이름 = " + createTopicResponse.topicArn());
            log.info("주제 list = " + snsClient.listTopics());

        }
    }

    // ------------------------------------------이메일----------------------------------------------------------

    @Override
    public String subscribeEmail(String topicArn, Long userId) {
        log.info("컨트 시작");

        String userEmail = getUserByEmail(userId);
        log.info("유저 아이디" + userEmail);

        SnsClient snsClient = credentialServiceImpl.getSnsClient();

        SubscribeRequest subscribeRequest = SubscribeRequest.builder()
                .protocol("email")
                .topicArn(topicArn)
                .endpoint(userEmail)
                .build();

        SubscribeResponse subscribeResponse = snsClient.subscribe(subscribeRequest);

        log.info("구독 request" + subscribeRequest);
        log.info("구독 Response" + subscribeResponse);

        if (!subscribeResponse.sdkHttpResponse().isSuccessful()) {
            throw new CustomException(AdminErrorCode.SUBSCRIBE_RESPONSE_FAILED, subscribeResponse);
        }

        // DTO에 정보 저장 (subscriptionArn은 아직 pending confirmation 상태로 가정)
        AlarmDTO alarmEmailInfo = saveAlarmInfo("pending confirmation", userEmail);

        log.info("구독 정보" + alarmEmailInfo);

        log.info("Subscribed member: " + userEmail);
        snsClient.close();

        return "이메일 구독 성공";
    }

    @Override
    public String confirmSubscription(String topicArn, Long userId) {

        // 사용자 정보 가져오기
        String userByEmail = getUserByEmail(userId);

        log.info("현재 유저 이메일: " + userByEmail);

        SnsClient snsClient = credentialServiceImpl.getSnsClient();

        // ListSubscriptionsByTopic으로 Subscription 정보 가져오기
        ListSubscriptionsByTopicRequest listRequest = ListSubscriptionsByTopicRequest.builder()
                .topicArn(topicArn)
                .build();

        ListSubscriptionsByTopicResponse listResponse = snsClient.listSubscriptionsByTopic(listRequest);

        for (Subscription subscription : listResponse.subscriptions()) {
            String subscriptionArn = subscription.subscriptionArn();
            String endpoint = subscription.endpoint();
            log.info(endpoint + "엔드포인트");
            log.info(subscriptionArn + "구독 arn");

            if (userByEmail.equals(endpoint)) {
                if ("PendingConfirmation".equals(subscriptionArn)) {
                    return "이메일 불일치";
                } else {
                    return "이메일 일치";
                }
            }
        }

        return "인증 성공";
    }

    @Override
    public String publishEmail(String topicArn, Map<String, String> payload) {
        try {
            SnsClient snsClient = credentialServiceImpl.getSnsClient();

            // "emailText" 키에 해당하는 값을 추출
            String emailText = payload.get("emailText");

            log.info("파싱전 이메일 내용" + emailText);

            // HTML에서 <p> 태그를 제거하고 텍스트만 추출
            String emailContent = removePTags(emailText);

            final PublishRequest publishRequest = PublishRequest.builder()
                    .topicArn(topicArn)
                    .subject("HTTP ENDPOINT TEST MESSAGE")
                    .message(emailContent)
                    .build();

            // SNS 주제에 메시지 발송
            PublishResponse publishResponse = snsClient.publish(publishRequest);

            snsClient.close();

            return publishResponse + "이메일 전송 성공";
        } catch (Exception e) {
            log.error("메세지 전송 에러: " + e.getMessage(), e);
            return "이메일 전송 실패";
        }
    }

    public String removePTags(String html) {
        // 정규식을 사용하여 <p> 태그를 제거하고 텍스트만 추출
        Pattern pattern = Pattern.compile("<p>(.*?)</p>");
        Matcher matcher = pattern.matcher(html);

        StringBuilder textContent = new StringBuilder();

        while (matcher.find()) {
            textContent.append(matcher.group(1)); // 매치된 그룹의 내용을 추가
        }

        return textContent.toString();
    }

    @Override
    public String unsubscribe(String topicArn, Long userId) {

        String userByEmail = getUserByEmail(userId);

        SnsClient snsClient = credentialServiceImpl.getSnsClient();

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
                    log.info("구독 취소 성공: " + subscription.subscriptionArn());
                    return "구독 취소 성공";
                } else {
                    throw new CustomException(AdminErrorCode.EMAIL_NOT_FOUND);
                }
            }
        }

        // 찾지 못한 경우
        return "구독 취소 실패";
    }

    @Override
    public String subscribeMessage(String topicArn, Long userId) {
        log.info("메세지");

        String memberPhone = getUserByPhone(userId);

        try (SnsClient snsClient = credentialServiceImpl.getSnsClient()) {
            final SubscribeRequest subscribeRequest = SubscribeRequest.builder()
                    .protocol("SMS")  // SMS 프로토콜
                    .topicArn(topicArn)
                    .endpoint("+82" + memberPhone)
                    .build();

            final SubscribeResponse subscribeResponse = snsClient.subscribe(subscribeRequest);

            if (!subscribeResponse.sdkHttpResponse().isSuccessful()) {
                throw new CustomException(AdminErrorCode.SUBSCRIBE_RESPONSE_FAILED, subscribeResponse);
            }

            log.info("주제안 구독 Arn = " + subscribeResponse.subscriptionArn());
            log.info("구독 리시트 = " + snsClient.listSubscriptions());

            return "메시지 구독 성공";
        }
    }

    @Override
    public String publishMessage(String topicArn, ResponseMessageDTO responseMessageDTO) {
        Long confirmedGameId = responseMessageDTO.getConfirmedGameId();

        log.info("arn++:" + topicArn);
        log.info(confirmedGameId + "확정된 아이디!");

        if (confirmedGameId == null) {
            throw new CustomException(AdminErrorCode.AWAY_APPLICANT_ID_NOT_FOUND);
        }

        try {
            Game game = gameManagementRepository.findById(confirmedGameId)
                    .orElseThrow(() -> new CustomException(AdminErrorCode.GAME_NOT_FOUND, confirmedGameId));

            String userPhone = game.getAwayApplicant().getPhone();
            log.info(userPhone + "첫 번호");
            String userPhoneNumber = "8201059673459";

            //  String userPhoneNumber = "+"+userPhone.replaceAll("-", "");
            log.info(userPhoneNumber + "가져온 번호");

            // ListSubscriptionsByTopic으로 Subscription 정보 가져오기
            ListSubscriptionsByTopicRequest listRequest = ListSubscriptionsByTopicRequest.builder()
                    .topicArn(topicArn)
                    .build();

            SnsClient snsClient = credentialServiceImpl.getSnsClient();
            ListSubscriptionsByTopicResponse listResponse = snsClient.listSubscriptionsByTopic(listRequest);

            boolean isUserSubscribed = false;

            for (Subscription subscription : listResponse.subscriptions()) {
                String subscriptionEndpointWithPlus = subscription.endpoint();

                // + 제외한 값 추출
                String subscriptionEndpoint = subscriptionEndpointWithPlus.replace("+", "");

                log.info("구독 엔드포인트 핸드폰" + subscriptionEndpointWithPlus);

                if (userPhoneNumber.equals(subscriptionEndpoint)) {
                    isUserSubscribed = true;
                    break;
                }
            }

            if (isUserSubscribed) {
                MessageDTO gameInfo = getGameInfo(confirmedGameId);
                String gameMessage = buildConfirmationMessage(gameInfo);

                log.info(gameMessage + "메시지");

                PublishRequest publishRequest = PublishRequest.builder()
                        .message(gameMessage)
                        .phoneNumber("+" + userPhoneNumber)
                        .build();

                log.info(PublishRequest.builder().phoneNumber(userPhoneNumber) + "뭐야");

                PublishResponse publishResponse = snsClient.publish(publishRequest);

                log.info("유저 번호 " + userPhoneNumber);
                log.info(gameMessage + " 회원에게 보낸 메시지");

                snsClient.close();

                return publishResponse + "메시지 전송 성공";
            } else {
                log.info("유저 번호가 일치하지 않아 메시지를 보내지 않습니다.");
                snsClient.close();
                return "유저 번호가 일치하지 않아 메시지를 보내지 않았습니다.";
            }
        } catch (Exception e) {
            log.error("메시지 전송 에러", e);
            return "메시지 전송 실패";
        }
    }


    @Override
    public String unsubscribeMessage(String topicArn, Long userId) {
        String userByPhone = getUserByPhone(userId);
        String userPhoneNumber = "+" + userByPhone.replaceAll("-", "");

        SnsClient snsClient = credentialServiceImpl.getSnsClient();

        // ListSubscriptionsByTopic으로 Subscription 정보 가져오기
        ListSubscriptionsByTopicRequest listRequest = ListSubscriptionsByTopicRequest.builder()
                .topicArn(topicArn)
                .build();

        ListSubscriptionsByTopicResponse listResponse = snsClient.listSubscriptionsByTopic(listRequest);

        for (Subscription subscription : listResponse.subscriptions()) {
            String subscriptionEndpointWithPlus = subscription.endpoint();

            // + 제외한 값 추출
            String subscriptionEndpoint = subscriptionEndpointWithPlus.replace("+", "");

            log.info("구독 엔드포인트 핸드폰" + subscriptionEndpoint);

            // 이메일과 endpoint 일치 여부 확인
            if (userPhoneNumber.equals(subscriptionEndpoint)) {
                UnsubscribeRequest unsubscribeRequest = UnsubscribeRequest.builder()
                        .subscriptionArn(subscription.subscriptionArn())
                        .build();

                UnsubscribeResponse unsubscribeResponse = snsClient.unsubscribe(unsubscribeRequest);

                snsClient.close();

                if (unsubscribeResponse.sdkHttpResponse().isSuccessful()) {

                    return "메시지 구독 취소 성공";
                } else {
                    throw new CustomException(AdminErrorCode.SUBSCRIBE_CANCEL_FAILED);
                }
            }
        }

        return "메시지 구독 취소 실패";
    }

    @Override
    @Transactional
    public void updateUserRole(Long userId) {
        userRepository.findById(userId)
                .ifPresent(user -> {
                    int currentRole = user.getRole();
                    int newRole = (currentRole == 0) ? 2 : 0;
                    user.messageAuth(newRole);
                });
    }
}