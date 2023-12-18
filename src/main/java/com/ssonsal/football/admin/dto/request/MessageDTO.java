package com.ssonsal.football.admin.dto.request;

import com.ssonsal.football.game.entity.Game;
import com.ssonsal.football.team.entity.Team;
import com.ssonsal.football.user.entity.User;
import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@ToString
@NoArgsConstructor

public class MessageDTO {

    private Long id;
    private Team hometeamId; // 홈팀
    private LocalDateTime schedule; // 경기 날짜
    private Integer gameTime;  // 경기 시간
    private String stadium;     // 경기장
    private Integer vsFormat;  // 매치 인원
    private Integer account;    // 매치 비용
    private User awayApplicantId; // 매치 신청자
    private String phone; // 신청자 폰 번호
    private Integer matchStatus; // 경기 상태 # 0: 대기, 1: 확정, 2: 종료
    private String messageComment; // 문자 글


    @Builder
    public MessageDTO(Long id, Team hometeamId, LocalDateTime schedule, Integer gameTime, String stadium,
                      Integer vsFormat, Integer account, User awayApplicantId, String phone, Integer matchStatus) {
        this.id=id;
        this.hometeamId=hometeamId;
        this.schedule = schedule;
        this.gameTime= gameTime;
        this.stadium= stadium;
        this.vsFormat=vsFormat;
        this.account=account;
        this.awayApplicantId=awayApplicantId;
        this.phone=phone;
        this.matchStatus = matchStatus;
    }

    public MessageDTO build() {
        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setMessageComment(messageComment);
        return messageDTO;
    }

    public static MessageDTO messageGameFactory(Game game) {
        return MessageDTO.builder()
                .id(game.getId())
                .schedule(game.getSchedule())
                .gameTime(game.getGameTime())
                .stadium(game.getStadium())
                .vsFormat(game.getVsFormat())
                .account(game.getAccount())
                .matchStatus(game.getMatchStatus())
                .build();

    }

    public static MessageDTO messageSubFactory(User user) {
        return MessageDTO.builder()
                .phone(user.getPhone())
                .build();
    }

 /*   public static MessageDTO messageUserFactory(User user) {
        return MessageDTO.builder()
                .phone(user.getPhone())
                .build();
    }*/

}
