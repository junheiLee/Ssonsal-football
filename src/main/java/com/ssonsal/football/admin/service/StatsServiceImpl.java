package com.ssonsal.football.admin.service;

import com.ssonsal.football.admin.dto.response.StatsDTO;
import com.ssonsal.football.admin.exception.AdminErrorCode;
import com.ssonsal.football.admin.repository.GameManagementRepository;
import com.ssonsal.football.admin.repository.UserManagementRepository;
import com.ssonsal.football.game.entity.Game;
import com.ssonsal.football.global.exception.CustomException;
import com.ssonsal.football.user.entity.User;
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
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {

    private final GameManagementRepository gameManagementRepository;
    private final UserManagementRepository userManagementRepository;

    public StatsDTO statsDTO(List<Game> monthlyGames) {
        long confirmedGameCount = monthlyGames.stream()
                .filter(game -> game.getMatchStatus() == 1)
                .filter(game -> game.getDeleteCode() == 2)
                .count();
        long cancelledGameCount = monthlyGames.stream()
                .filter(game -> game.getMatchStatus() == 0)
                .filter(game -> game.getDeleteCode() == 2)
                .count();

        long totalGameCount = monthlyGames.size();


        return StatsDTO.builder()
                .cancelledGameCount(cancelledGameCount)
                .confirmedGameCount(confirmedGameCount)
                .totalGameCount(totalGameCount)
                .build();

    }

    @Override
    @Transactional
    public StatsDTO monthStats(LocalDate currentDate) {

        if (currentDate == null) {
            throw new CustomException(AdminErrorCode.DATE_NOT_FOUND);
        }

        int year = currentDate.getYear();
        int month = currentDate.getMonthValue();

        LocalDate startDate = LocalDate.of(year, month, 1);

        LocalDate endDate = startDate.with(TemporalAdjusters.lastDayOfMonth());

        List<Game> monthlyGames = gameManagementRepository.findByScheduleBetween(
                startDate.atStartOfDay(), endDate.atTime(23, 59, 59));


        if (monthlyGames == null) {
            throw new CustomException(AdminErrorCode.GAME_NOT_FOUND);
        }


        // StatsDTO를 활용하여 DTO 생성
        return statsDTO(monthlyGames);
    }

    public StatsDTO dayStats(LocalDate date, List<Game> dailyGames) {
        long confirmedGameCount = calculateGameCount(date, date.plusDays(1), dailyGames, 1);
        long cancelledGameCount = calculateGameCount(date, date.plusDays(1), dailyGames, 0);

        return StatsDTO.builder()
                .cancelledGameCount(cancelledGameCount)
                .confirmedGameCount(confirmedGameCount)
                .totalGameCount(cancelledGameCount + confirmedGameCount)
                .build();
    }

    @Transactional
    public void updateDeleteCodeForPastGames() {
        try {
            LocalDateTime currentDate = LocalDateTime.now();
            gameManagementRepository.updateDeleteCode(currentDate);
        } catch (CustomException e) {
            throw new CustomException(AdminErrorCode.DELETEDCODE_UPDATE_FAILED, e);
        }
    }

    @Transactional
    public long calculateGameCount(LocalDate startDate, LocalDate endDate, List<Game> dailyGames, int matchStatus) {

        return dailyGames.stream()
                .filter(game -> game.getDeleteCode() == 2 && game.getModifiedAt() != null &&
                        game.getModifiedAt().isAfter(startDate.atStartOfDay()) &&
                        game.getModifiedAt().isBefore(endDate.atTime(LocalTime.MAX)) &&
                        game.getMatchStatus() == matchStatus)
                .count();
    }


    @Override
    @Transactional
    public Map<LocalDate, StatsDTO> monthlyDailyStats(LocalDate currentDate) {

        if (currentDate == null) {
            throw new CustomException(AdminErrorCode.DATE_NOT_FOUND);
        }

        LocalDate startDate = currentDate.withDayOfMonth(1);
        LocalDate endDate = currentDate.withDayOfMonth(currentDate.lengthOfMonth());
        Map<LocalDate, StatsDTO> monthDayStats = new HashMap<>();

        updateDeleteCodeForPastGames();

        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            List<Game> dailyGames = gameManagementRepository.findByScheduleBetween(
                    date.atStartOfDay(), date.with(TemporalAdjusters.lastDayOfMonth()).atTime(23, 59, 59));

            StatsDTO dailyStats = dayStats(date, dailyGames);
            monthDayStats.put(date, dailyStats);
        }

        return monthDayStats;
    }

    public long getTotalUserCount() {
        return userManagementRepository.findAll().size();
    }

    public long getNewUserCount() {
        LocalDateTime today = LocalDateTime.now();
        LocalDateTime startOfDay = today.withHour(0).withMinute(0).withSecond(0).withNano(0);

        List<User> newUsers = userManagementRepository.findByCreatedAtBetween(startOfDay, today);
        return newUsers.size();
    }

    public long getNewPostCount() {
        LocalDateTime today = LocalDateTime.now();
        LocalDateTime startOfDay = today.withHour(0).withMinute(0).withSecond(0).withNano(0);

        List<Game> newPosts = gameManagementRepository.findByCreatedAtBetween(startOfDay, today);
        return newPosts.size();
    }

    public long getTodayGamesCount() {
        LocalDateTime now = LocalDateTime.now();

        List<Game> todayGames = gameManagementRepository.findAll().stream()
                .filter(game -> game.getMatchStatus() == 1 && game.getSchedule().toLocalDate().isEqual(now.toLocalDate()))
                .collect(Collectors.toList());
        return todayGames.size();
    }

    @Override
    public Map<String, Long> getAdminStats() {
        Map<String, Long> adminStatsMap = new HashMap<>();
        adminStatsMap.put("totalUserCount", getTotalUserCount());
        adminStatsMap.put("newUserCount", getNewUserCount());
        adminStatsMap.put("todayGameCount", getTodayGamesCount());
        adminStatsMap.put("newPostCount", getNewPostCount());

        return adminStatsMap;
    }

}