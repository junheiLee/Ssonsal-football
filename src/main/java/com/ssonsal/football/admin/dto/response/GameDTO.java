package com.ssonsal.football.admin.dto.response;

import com.ssonsal.football.game.entity.Game;
import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@ToString
@NoArgsConstructor
public class GameDTO {

    private Long id;
    private LocalDateTime createdAt;
    private String writer;
    private Integer matchStatus;
    private Integer vsFormat;
    private String stadium;
    private LocalDateTime schedule;

    @Builder
    public GameDTO(Long id, LocalDateTime createdAt, String writer, Integer matchStatus,
                   Integer vsFormat, String stadium, LocalDateTime schedule) {
        this.id = id;
        this.createdAt = createdAt;
        this.writer = writer;
        this.matchStatus = matchStatus;
        this.vsFormat = vsFormat;
        this.stadium = stadium;
        this.schedule = schedule;


    }


    public static GameDTO gameFactory(Game game) {
        return GameDTO.builder()
                .id(game.getId())
                .createdAt(game.getCreatedAt())
                .writer(game.getWriter().getNickname())
                .matchStatus(game.getMatchStatus())
                .vsFormat(game.getVsFormat())
                .stadium(game.getStadium())
                .schedule(game.getSchedule())
                .build();
    }

}




