package com.ssonsal.football.admin.service;

import com.ssonsal.football.admin.dto.request.ResponseMessageDTO;
import com.ssonsal.football.admin.dto.response.AlarmDTO;
import com.ssonsal.football.admin.dto.response.MessageDTO;

import java.util.Map;

public interface AlarmService {

    /**
     * 이메일이 전송될 내용들
     * 게임 매치가 확정이 난 경기만 메세지를 보낼 수 있고
     * 홈팀 이름, 경기 날짜 및 시간, 경기 시간, 경기장, 경기 인원, 비용들을 보낸다
     *
     * @param confirmedGameId 이를 통해 확정된 경기인지 판별한다
     * @return 확정된 경기면 보낼 내용을 DTO에 저장한다
     */
    MessageDTO getGameInfo(Long confirmedGameId);

    /**
     * 알람 정보 저장
     *
     * @param subscriptionArn
     * @param userEmail       위 값들을 DTO에 저장해서 구독 확인을 위해 쓴다
     * @return alarmDTO에 저장
     */
    AlarmDTO saveAlarmInfo(String subscriptionArn, String userEmail);

    /**
     * 유저의 이메일 찾기
     *
     * @param userId userId를 통해 찾기
     * @return 유저의 이메일을 찾아 반환한다
     */
    String getUserByEmail(Long userId);

    /**
     * 유저의 번호 찾기
     *
     * @param userId userId를 통해 찾기
     * @return 유저의 번호을 찾아 반환한다
     */
    String getUserByPhone(Long userId);

    /**
     * 보낼 내용 작성
     * getGameInfo를 통해 저장된 내용들을
     * 사용자의 경기가 확정되면 해당 내용들을 보낸다
     *
     * @param gameInfo 경기 정보를 가져온다
     * @return 성공 메세지와 경기 정보들을 jsonMessage로 한 객체에 저장한다
     */
    String buildConfirmationMessage(MessageDTO gameInfo);

    /**
     * 주제 생성
     * AWS SNS와 통신하여 AWS SNS에 주제가 생성된다
     * 주제가 생성되면 회원들의 이메일과 sms 리스트가
     * 만들어진다고 생각하면 된다
     *
     * @param topicName 생성한 주제 이름
     * @return 성공 메시지와 주제 성공 응답 반환
     */
    String createTopic(String topicName);

    // ------------------------------------------이메일----------------------------------------------------------

    /**
     * 이메일 구독 생성 기능
     * 구독을 생성하면
     * subscriptionArn와 userEmail을 저장한다
     *
     * @param topicArn
     * @param userId
     * @return 성공 메세지와 구독 응답
     */
    String subscribeEmail(String topicArn, Long userId);

    /**
     * 구독 이메일 인증 확인
     *
     * @param topicArn
     * @param userId   가져온 정보들로 aws sns에 저장된 값과 비교하여 구독 확인을 진행한다
     * @return
     */
    String confirmSubscription(String topicArn, Long userId);

    /**
     * 이메일 보내기
     *
     * @param topicArn 이메일은 구독된 전체 회원에게 이메일이 보내진다
     *                 메세지 내용을 가지고와 그 text로 이메일이 보내진다
     * @return 메세지와 메시지id
     */
    String publishEmail(String topicArn, Map<String, String> payload);

    /**
     * 이메일 구독 취소
     * 이메일 수신을 원하지 않는 사용자들의 구독을 취소시킨다
     *
     * @param topicArn
     * @param userId   userId로 해당 유저의 이메일을 가지고와
     *                 userId의 이메일은 고유값이니
     *                 userId의 이메일과 aws 주제안에 있는 이메일이 일치하면 구독을 취소시킨다
     * @return
     */
    String unsubscribe(String topicArn, Long userId);

    // ------------------------------------------메시지----------------------------------------------------------

    /**
     * 메세지 구독
     *
     * @param topicArn
     * @param userId
     * @return
     */
    String subscribeMessage(String topicArn, Long userId);

    /**
     * 메세지 전송
     *
     * @param topicArn
     * @param responseMessageDTO
     * @return
     */
    String publishMessage(String topicArn, ResponseMessageDTO responseMessageDTO);

    /**
     * <p> </p> 이거 같은 태그 제거
     * dependency 추가함
     *
     * @param html
     * @return
     */
    public String removePTags(String html);

    /**
     * 메세지 수신 취소
     * 이메일 수신을 원하지 않는 사용자들의 구독을 취소시킨다
     *
     * @param topicArn
     * @param userId   userId로 해당 유저의 번호을 가지고와
     *                 userId의 번호은 고유값이니
     *                 userId의 번호 aws 주제안에 있는 번호이 일치하면 구독을 취소시킨다
     */
    String unsubscribeMessage(String topicArn, Long userId);
}
