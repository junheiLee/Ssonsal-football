package com.ssonsal.football.admin.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssonsal.football.admin.dto.request.AlarmDTO;
import com.ssonsal.football.admin.dto.request.EmailDTO;
import com.ssonsal.football.admin.dto.request.MessageDTO;
import com.ssonsal.football.admin.entity.Alarm;
import com.ssonsal.football.admin.repository.AlarmRepository;
import com.ssonsal.football.admin.repository.GameManagementRepository;
import com.ssonsal.football.admin.repository.UserManagementRepository;
import com.ssonsal.football.game.entity.Game;
import com.ssonsal.football.global.exception.CustomException;
import com.ssonsal.football.global.util.ErrorCode;
import com.ssonsal.football.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.ssonsal.football.game.util.GameConstant.USER_ID;
import static com.ssonsal.football.game.util.Transfer.longIdToMap;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class AlarmService {
    private final GameManagementRepository gameManagementRepository;
    private final UserManagementRepository userManagementRepository;
    private final AlarmRepository alarmRepository;

    public String getUserByEmail(Long userId) {
        Optional<User> userOptional = userManagementRepository.findById(userId);

        return userOptional.map(User::getEmail).orElse(null);
    }



    public MessageDTO getGameInfo(Long confirmedGameId) throws GameNotFoundException {
        Optional<Game> gameOptional = gameManagementRepository.findById(confirmedGameId);

        if (gameOptional.isPresent()) {
            Game game = gameOptional.get();

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
                throw new GameNotFoundException("게임 매치가 확정이 아닙니다: " + confirmedGameId);
            }
        } else {
            throw new GameNotFoundException("게임을 찾을 수 없습니다: " + confirmedGameId);
        }
    }

    @Transactional
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

            return jsonMessage;

        } catch (Exception e) {
            log.error("처리 중 오류 발생", e);
            return "";
        }
    }

    @Transactional
    public List<EmailDTO> memberList() {
        List<EmailDTO> memberList = userManagementRepository.findAllMember();

        return memberList;
    }

    @Transactional
    public AlarmDTO saveAlarmInfo(String topicArn, String subscriptionArn,String userEmail) {
        AlarmDTO alarmDTO = AlarmDTO.builder()
                .topicArn(topicArn)
                .subscriptionArn(subscriptionArn)
                .userEmail(userEmail)
                .build();

        Alarm alarmEntity = Alarm.builder()
                .topicArn(alarmDTO.getTopicArn())
                .subscriptionArn(alarmDTO.getSubscriptionArn())
                .userEmail(alarmDTO.getUserEmail())
                .build();

        alarmRepository.save(alarmEntity);


        return alarmDTO;
    }

    @Transactional
    public void confirmSubscription(String topicArn, String subscriptionArn) {
        Optional<Alarm> existingAlarm = alarmRepository.findByTopicArnAndSubscriptionArn(topicArn, subscriptionArn);

        existingAlarm.ifPresent(alarmEntity -> {
            // 기존 알람 정보가 있는 경우
            Alarm updatedAlarm = Alarm.builder()
                    .topicArn(topicArn)
                    .subscriptionArn(subscriptionArn)
                    .build();
            alarmRepository.save(updatedAlarm);
        });

        // 찾지 못한 경우에는 새로운 알람 정보 생성 및 저장
        Alarm newAlarm = Alarm.builder()
                .topicArn(topicArn)
                .subscriptionArn(subscriptionArn)
                .build();
        alarmRepository.save(newAlarm);
    }

    public String getUserByPhone(Long userId) {
        Optional<User> userOptional = userManagementRepository.findById(userId);

        return userOptional.map(User::getPhone).orElse(null);
    }
}