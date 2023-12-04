package com.ssonsal.football.admin.dto.request;

import com.ssonsal.football.game.entity.Game;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
public class StatsDTO {

    private Long id;
    private Integer matchStatus;
    private LocalDateTime schedule;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private Integer deleteCode;
    private Long cancelledGameCount;
    private Long confirmedGameCount;
    private Long totalGameCount;

    @Builder
    public StatsDTO(Long id, Integer matchStatus, LocalDateTime schedule, LocalDateTime createdAt,
                    LocalDateTime modifiedAt, Integer deleteCode, Long cancelledGameCount, Long confirmedGameCount,
                    Long totalGameCount) {
        this.id = id;
        this.matchStatus = matchStatus;
        this.schedule = schedule;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
        this.deleteCode = deleteCode;
        this.cancelledGameCount = cancelledGameCount;
        this.confirmedGameCount = confirmedGameCount;
        this.totalGameCount = totalGameCount;
    }

    // 엔티티에 없는 변수들
    public StatsDTO build() {
        StatsDTO statsDTO = new StatsDTO();
        statsDTO.setCancelledGameCount(cancelledGameCount);
        statsDTO.setConfirmedGameCount(confirmedGameCount);
        statsDTO.setTotalGameCount(totalGameCount);
        return statsDTO;
    }

    public static StatsDTO statsFactory(Game game) {
        long cancelledGameCount = game.getMatchStatus() == 0 ? 1L : 0L;
        long confirmedGameCount = game.getMatchStatus() == 1 ? 1L : 0L;
        long totalGameCount = 0L;
        return StatsDTO.builder()
                .id(game.getId())
                .matchStatus(game.getMatchStatus())
                .schedule(game.getSchedule())
                .createdAt(game.getCreatedAt())
                .modifiedAt(game.getModifiedAt())
                .deleteCode(game.getDeleteCode())
                .cancelledGameCount(cancelledGameCount)
                .confirmedGameCount(confirmedGameCount)
                .totalGameCount(totalGameCount)
                .build();
    }

    @Override
    public String toString() {
        return "StatsDTO{" +
                " 매치 상태=" + matchStatus +
                ", 일정=" + schedule +
                ", createdAt=" + createdAt +
                ", modifiedAt=" + modifiedAt +
                ", deleteCode=" + deleteCode +
                ", cancelledGameCount=" + cancelledGameCount +
                ", confirmedGameCount=" + confirmedGameCount +
                ", totalGameCount=" + totalGameCount +
                '}';
    }
}
