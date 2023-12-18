package com.ssonsal.football.admin.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssonsal.football.admin.dto.request.AlarmDTO;
import com.ssonsal.football.admin.dto.request.MessageDTO;
import com.ssonsal.football.admin.dto.response.ResponseMessageDTO;
import com.ssonsal.football.admin.exception.AdminErrorCode;
import com.ssonsal.football.admin.repository.GameManagementRepository;
import com.ssonsal.football.game.entity.Game;
import com.ssonsal.football.global.exception.CustomException;
import com.ssonsal.football.user.entity.User;
import com.ssonsal.football.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.*;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class AlarmService {
    private final GameManagementRepository gameManagementRepository;
    private final UserRepository userRepository;
    private final CredentialService credentialService;


    /**
     * 이메일이 전송될 내용들
     * 게임 매치가 확정이 난 경기만 메세지를 보낼 수 있고
     * 홈팀 이름, 경기 날짜 및 시간, 경기 시간, 경기장, 경기 인원, 비용들을 보낸다
     *
     * @param confirmedGameId 이를 통해 확정된 경기인지 판별한다
     * @return 확정된 경기면 보낼 내용을 DTO에 저장한다
     */
    public MessageDTO getGameInfo(Long confirmedGameId) {
        Game game = gameManagementRepository.findById(confirmedGameId)
                .orElseThrow(() -> new CustomException(AdminErrorCode.GAME_NOT_FOUND, confirmedGameId));

        if (game.getMatchStatus() == 1) {
            return MessageDTO.builder()
                    .hometeamId(game.getHome())
                    .schedule(game.getSchedule())
                    .gameTime(game.getGameTime())
                    .stadium(game.getStadium())
                    .vsFormat(game.getVsFormat())
                    .account(game.getAccount())
                    .build();
        } else {
            throw new CustomException(AdminErrorCode.GAME_NOT_CONFIRMED, confirmedGameId);
        }
    }

    /**
     * 알람 정보 저장
     *
     * @param subscriptionArn
     * @param userEmail       위 값들을 DTO에 저장해서 구독 확인을 위해 쓴다
     * @return alarmDTO에 저장
     */

    public AlarmDTO saveAlarmInfo(String subscriptionArn, String userEmail) {
        AlarmDTO alarmDTO = AlarmDTO.builder()
                .subscriptionArn(subscriptionArn)
                .userEmail(userEmail)
                .build();

        return alarmDTO;
    }

    /**
     * 유저의 이메일 찾기
     *
     * @param userId userId를 통해 찾기
     * @return 유저의 이메일을 찾아 반환한다
     */
    public String getUserByEmail(Long userId) {

        return userRepository.findById(userId)
                .map(User::getEmail)
                .orElseThrow(() -> new CustomException(AdminErrorCode.USER_NOT_FOUND, userId));
    }

    /**
     * 유저의 번호 찾기
     *
     * @param userId userId를 통해 찾기
     * @return 유저의 번호을 찾아 반환한다
     */
    public String getUserByPhone(Long userId) {
        return userRepository.findById(userId)
                .map(User::getPhone)
                .orElseThrow(() -> new CustomException(AdminErrorCode.USER_NOT_FOUND, userId));
    }

    /**
     * 보낼 내용 작성
     * getGameInfo를 통해 저장된 내용들을
     * 사용자의 경기가 확정되면 해당 내용들을 보낸다
     *
     * @param gameInfo 경기 정보를 가져온다
     * @return 성공 메세지와 경기 정보들을 jsonMessage로 한 객체에 저장한다
     */
    public String buildConfirmationMessage(MessageDTO gameInfo) {
        log.info("문자 데이터" + gameInfo);

        try {
            ObjectMapper objectMapper = new ObjectMapper();

            String message = "" + gameInfo.getHometeamId().getName() + " 팀과의 매치가 확정되었습니다.\n" +
                    "매치 날짜: " + gameInfo.getSchedule() + "\n" +
                    "매치 시간: " + gameInfo.getGameTime() + " 시간\n" +
                    "경기장: " + gameInfo.getStadium() + "\n" +
                    "플레이어: " + gameInfo.getVsFormat() + "\n" +
                    "비용: " + gameInfo.getAccount();

            log.info("메세지 내용: {}", message);

            String jsonMessage = objectMapper.writeValueAsString(Map.of("message", message));

            log.info("JSON Message: {}", jsonMessage);

            return  jsonMessage;

        } catch (Exception e) {
            log.error("처리 중 오류 발생", e);
            return "오류 발생";
        }
    }

    /**
     * 주제 생성
     * AWS SNS와 통신하여 AWS SNS에 주제가 생성된다
     * 주제가 생성되면 회원들의 이메일과 sms 리스트가
     * 만들어진다고 생각하면 된다
     *
     * @param topicName 생성한 주제 이름
     * @return 성공 메시지와 주제 성공 응답 반환
     */
    public String createTopic(String topicName) {
        SnsClient snsClient = credentialService.getSnsClient();

        try {
            final CreateTopicRequest createTopicRequest = CreateTopicRequest.builder()
                    .name(topicName)
                    .build();
            final CreateTopicResponse createTopicResponse = snsClient.createTopic(createTopicRequest);

            if (!createTopicResponse.sdkHttpResponse().isSuccessful()) {
                throw new CustomException(AdminErrorCode.TOPIC_RESPONSE_FAILED, createTopicResponse);
            }

            log.info("주제 이름 = " + createTopicResponse.topicArn());
            log.info("주제 list = " + snsClient.listTopics());

            return "주제 생성 성공";
        } finally {
            snsClient.close();
        }
    }

    // ------------------------------------------이메일----------------------------------------------------------


    /**
     * 이메일 구독 생성 기능
     * 구독을 생성하면
     * subscriptionArn와 userEmail을 저장한다
     * @param topicArn
     * @param userId
     * @return 성공 메세지와 구독 응답
     */
    public String subscribeEmail(String topicArn, Long userId) {
        log.info("컨트 시작");

        String userEmail = getUserByEmail(userId);
        log.info("유저 아이디" + userEmail);

        SnsClient snsClient = credentialService.getSnsClient();

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

    /**
     * 구독 이메일 인증 확인
     * @param topicArn
     * @param userId
     * 가져온 정보들로 aws sns에 저장된 값과 비교하여 구독 확인을 진행한다
     * @return
     */
    public String confirmSubscription(String topicArn, Long userId) {

        // 사용자 정보 가져오기
        String userByEmail = getUserByEmail(userId);

        log.info("현재 유저 이메일: " + userByEmail);

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
            log.info(endpoint + "엔드포인트");
            log.info(subscriptionArn + "구독 arn");

            // 이메일과 endpoint 일치 여부 확인
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

    /**
     * 이메일 보내기
     * @param topicArn
     * 이메일은 구독된 전체 회원에게 이메일이 보내진다
     * 메세지 내용을 가지고와 그 text로 이메일이 보내진다
     * @return 메세지와 메시지id
     */
    public String publishEmail(String topicArn, Map<String, String> payload) {
        try {
            SnsClient snsClient = credentialService.getSnsClient();

            // "emailText" 키에 해당하는 값을 추출
            String emailText = payload.get("emailText");

            log.info("파싱전 이메일 내용"+emailText);

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

            return "이메일 전송 성공";
        } catch (Exception e) {
            log.error("메세지 전송 에러: " + e.getMessage(), e);
            return "이메일 전송 실패";
        }
    }

    /**
     *  <p> </p> 이거 같은 태그 제거
     *  dependency 추가함
     * @param html
     * @return
     */

    private String removePTags(String html) {
        // 정규식을 사용하여 <p> 태그를 제거하고 텍스트만 추출
        Pattern pattern = Pattern.compile("<p>(.*?)</p>");
        Matcher matcher = pattern.matcher(html);

        StringBuilder textContent = new StringBuilder();

        while (matcher.find()) {
            textContent.append(matcher.group(1)); // 매치된 그룹의 내용을 추가
        }

        return textContent.toString();
    }

    /**
     * 이메일 구독 취소
     * 이메일 수신을 원하지 않는 사용자들의 구독을 취소시킨다
     * @param topicArn
     * @param userId
     * userId로 해당 유저의 이메일을 가지고와
     * userId의 이메일은 고유값이니
     * userId의 이메일과 aws 주제안에 있는 이메일이 일치하면 구독을 취소시킨다
     * @return
     */
    public String unsubscribe(String topicArn,Long userId) {

        String userByEmail = getUserByEmail(userId);

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

// ------------------------------------------메시지----------------------------------------------------------

    /**
     * 메세지 구독
     * @param topicArn
     * @param userId
     * @return
     */
    public String subscribeMessage(String topicArn, Long userId) {
        log.info("메세지");

        String userPhone = getUserByPhone(userId);
        String memberPhone = userPhone;

        SnsClient snsClient = credentialService.getSnsClient();

        try {
            final SubscribeRequest subscribeRequest = SubscribeRequest.builder()
                    .protocol("SMS")  // SMS 프로토콜
                    .topicArn(topicArn)
                    .endpoint(memberPhone)
                    .build();

            final SubscribeResponse subscribeResponse = snsClient.subscribe(subscribeRequest);

            log.info("회원들 번호" + memberPhone);
            if (!subscribeResponse.sdkHttpResponse().isSuccessful()) {
                throw new CustomException(AdminErrorCode.SUBSCRIBE_RESPONSE_FAILED, subscribeResponse);
            }

            log.info("주제안 구독 Arn = " + subscribeResponse.subscriptionArn());
            log.info("구독 리시트 = " + snsClient.listSubscriptions());

            return "메시지 구독 성공";
        } finally {
            snsClient.close();
        }
    }

    /**
     * 메세지 전송
     * @param topicArn
     * @param responseMessageDTO
     * @return
     */
    public String publishMessage(String topicArn, ResponseMessageDTO responseMessageDTO) {
        Long confirmedGameId = responseMessageDTO.getConfirmedGameId();
        if (confirmedGameId == null) {
            throw new CustomException(AdminErrorCode.GAME_NOT_FOUND);
        }

        try {
            MessageDTO gameInfo = getGameInfo(confirmedGameId);

            // 메시지 작성
            String gameMessage = buildConfirmationMessage(gameInfo);

            // SNS 클라이언트 및 전송
            SnsClient snsClient = credentialService.getSnsClient();
            PublishRequest publishRequest = PublishRequest.builder()
                    .topicArn(topicArn)
                    .message(gameMessage)
                    .build();

            PublishResponse publishResponse = snsClient.publish(publishRequest);

            snsClient.close();

           return "메시지 전송 성공";
        } catch (Exception e) {
            log.error("메시지 전송 에러", e);
            return "메시지 전송 실패";
        }
    }

    /**
     * 메세지 수신 취소
     * 이메일 수신을 원하지 않는 사용자들의 구독을 취소시킨다
     * @param topicArn
     * @param userId
     * userId로 해당 유저의 번호을 가지고와
     * userId의 번호은 고유값이니
     * userId의 번호 aws 주제안에 있는 번호이 일치하면 구독을 취소시킨다
     */
    public String unsubscribeMessage(String topicArn,Long userId) {
        String userByPhone = getUserByPhone(userId);
        String userPhoneNumber = userByPhone.replaceAll("-", "");

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
}