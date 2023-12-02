package com.ssonsal.football.admin.service;

import com.ssonsal.football.admin.dto.request.StatsDTO;
import com.ssonsal.football.admin.repository.GameManagementRepository;
import com.ssonsal.football.game.entity.Game;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StatsService {


    private final GameManagementRepository gameManagementRepository;

    // 한달 데이터 DTO생성
    private StatsDTO statsDTO(List<Game> monthlyGames) {
        long confirmedGameCount = monthlyGames.stream().filter(game -> game.getMatchStatus() == 1).count();
        long cancelledGameCount = monthlyGames.stream().filter(game -> game.getMatchStatus() == 0).count();

        long totalGameCount = cancelledGameCount + confirmedGameCount;

        log.info("달별 성사 경기" + confirmedGameCount);
        log.info("달별 취소 경기" + cancelledGameCount);

        StatsDTO statsDTO = StatsDTO.builder()
                .cancelledGameCount(cancelledGameCount)
                .confirmedGameCount(confirmedGameCount)
                .totalGameCount(totalGameCount)
                .build();

        log.info("달별 취소 경기" + statsDTO);

        return statsDTO;


    }

    // 날짜 데이터(한달 구하는 로직)
    public StatsDTO monthStats(LocalDate currentDate) {

        int year = currentDate.getYear();
        int month = currentDate.getMonthValue();

        LocalDate startDate = LocalDate.of(year, month, 1);

        LocalDate endDate = startDate.with(TemporalAdjusters.lastDayOfMonth());

        List<Game> monthlyGames = gameManagementRepository.findByScheduleBetween(
                startDate.atStartOfDay(), endDate.atTime(23, 59, 59));

        log.info("처음부터" + startDate.atStartOfDay());
        log.info("마지막" + endDate.atTime(LocalTime.MAX));

        // StatsDTO를 활용하여 DTO 생성
        return statsDTO(monthlyGames);
    }

    // 월간 일별 데이터 DTO 생성
    @Transactional
    private StatsDTO dayStats(LocalDate date, List<Game> dailyGames) {
        long confirmedGameCount = calculateGameCountForDateRange(date, date.plusDays(1), dailyGames, 1);
        long cancelledGameCount = calculateGameCountForDateRange(date, date.plusDays(1), dailyGames, 0);

        StatsDTO statsDTO = StatsDTO.builder()
                .cancelledGameCount(cancelledGameCount)
                .confirmedGameCount(confirmedGameCount)
                .totalGameCount(cancelledGameCount + confirmedGameCount)
                .build();

        return statsDTO;
    }

    // 날짜 데이터(하루 구하는 로직)
    private List<Game> getDailyStatsForDate(LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay(); // 해당 일의 00:00:00
        LocalDateTime endOfDay = date.atTime(23, 59, 59); // 해당 일 마지막 시간 23:59:59.99999

        return gameManagementRepository.findByScheduleBetween(startOfDay, endOfDay);
    }

    // 스캐줄이 지나면 딜리트코드 2로 바뀜
    @Transactional
    public void updateDeleteCodeForPastGames() {
        LocalDateTime currentDate = LocalDateTime.now();
        gameManagementRepository.updateDeleteCode(currentDate);
    }

    // 현재 달 계산
    @Transactional
    public Map<LocalDate, StatsDTO> monthlyDailyStats(LocalDate currentDate) {

        LocalDate startDate = currentDate.withDayOfMonth(1);

        LocalDate endDate = currentDate.withDayOfMonth(currentDate.lengthOfMonth());
        Map<LocalDate, StatsDTO> monthDayStats = new HashMap<>();

        updateDeleteCodeForPastGames();
        // 시작 날짜부터 종료 날짜까지 루프
        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            // 해당 날짜의 게임을 가져옴
            List<Game> dailyGames = getDailyStatsForDate(date);


            StatsDTO dailyStats = dayStats(date, dailyGames);
            monthDayStats.put(date, dailyStats);
        }

        return monthDayStats;
    }

    // calculateGameCountForDateRange 데이터 값 생성
    @Transactional
    private long calculateGameCountForDateRange(LocalDate startDate, LocalDate endDate, List<Game> dailyGames, int matchStatus) {
        return dailyGames.stream()
                .filter(game -> game.getDeleteCode() == 2 && game.getModifiedAt() != null &&
                        game.getModifiedAt().isAfter(startDate.atStartOfDay()) &&
                        game.getModifiedAt().isBefore(endDate.atTime(LocalTime.MAX)) &&
                        game.getMatchStatus() == matchStatus)
                .count();
    }

}