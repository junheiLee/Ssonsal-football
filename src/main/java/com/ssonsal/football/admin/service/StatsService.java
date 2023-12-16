package com.ssonsal.football.admin.service;

import com.ssonsal.football.admin.dto.request.GameDTO;
import com.ssonsal.football.admin.dto.request.StatsDTO;
import com.ssonsal.football.admin.dto.request.UserCountDTO;
import com.ssonsal.football.admin.exception.AdminErrorCode;
import com.ssonsal.football.admin.repository.GameManagementRepository;
import com.ssonsal.football.admin.repository.UserManagementRepository;
import com.ssonsal.football.game.entity.Game;
import com.ssonsal.football.global.exception.CustomException;
import com.ssonsal.football.user.entity.User;
import com.ssonsal.football.user.repository.UserRepository;
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
public class StatsService {

    private final GameManagementRepository gameManagementRepository;
    private final UserManagementRepository userManagementRepository;

    //--------------------------- 한달 데이터 -----------------------------------

    /**
     * 확정된 경기와 취소된 경기 데이터를 DTO에 저장하는 로직
     * 전체
     * request: game.getMatchStatus의 0 또는 1 을 가져온다
     * response: statsDTO에 저장
     */
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

        return statsDTO;

    }

    /**
     * 날짜 데이터(한달 구하는 로직)
     * 계산된 확정 데이터와 취소 데이터의 범위를 설정해준다
     * 기준을 한달로 잡고 현재 날짜까지만 통계로 보여진다
     * request: game.getMatchStatus의 0 또는 1 을 가져온다
     * response: statsDTO에 저장
     */
    @Transactional
    public StatsDTO monthStats(LocalDate currentDate) {

        if (currentDate == null) {
            throw new CustomException(AdminErrorCode.DATE_NOT_FOUND);
        }

        int year = currentDate.getYear();


        int month = currentDate.getMonthValue();

        LocalDate startDate = LocalDate.of(year, month, 1);

        log.info("시작날" + startDate);

        LocalDate endDate = startDate.with(TemporalAdjusters.lastDayOfMonth());
        log.info("마지막날" + endDate);

        List<Game> monthlyGames = gameManagementRepository.findByScheduleBetween(
                startDate.atStartOfDay(), endDate.atTime(23, 59, 59));


        if (monthlyGames == null) {
            throw new CustomException(AdminErrorCode.GAME_NOT_FOUND);
        }


        // StatsDTO를 활용하여 DTO 생성
        return statsDTO(monthlyGames);
    }

    //--------------------------- 하루 데이터 -----------------------------------

    /**
     * 월간 일별 데이터 DTO 생성
     * 제공된 게임 목록 dailyGames을 기반으로
     * 해당 날짜에 대한 확정된 경기 수 및 취소된 경기 수를 계산
     * request: 지정한 날짜를 가져온다
     * response: 지정된 날짜의 확정 경기와 취소 경기를 DTO에 담는다
     */


    private StatsDTO dayStats(LocalDate date, List<Game> dailyGames) {
        long confirmedGameCount = calculateGameCount(date, date.plusDays(1), dailyGames, 1);
        long cancelledGameCount = calculateGameCount(date, date.plusDays(1), dailyGames, 0);

        StatsDTO statsDTO = StatsDTO.builder()
                .cancelledGameCount(cancelledGameCount)
                .confirmedGameCount(confirmedGameCount)
                .totalGameCount(cancelledGameCount + confirmedGameCount)
                .build();

        return statsDTO;
    }


    /**
     * 날짜가 지나면 deleteCode 변경
     * deleteCode를 변경시키면서  modifiedAt으로 변경된 시점의 데이터들을 뽑을 수 있다
     * 현재 날짜와 시간을 사용하여 날짜가 지나면 게임의 정보를 변경 시키는데
     * deleteCode를 2로 변경 시킨다.
     * <p>
     * request: 하루 데이터를 가져온다
     * response: 하루가 지나면 deleteCode는 2로 변경된다
     */

    @Transactional
    public void updateDeleteCodeForPastGames() {
        try {
            LocalDateTime currentDate = LocalDateTime.now();
            gameManagementRepository.updateDeleteCode(currentDate);
        } catch (CustomException e) {
            throw new CustomException(AdminErrorCode.DELETEDCODE_UPDATE_FAILED, e);
        }
    }

    /**
     * 하루의 게임 수 계산
     * 삭제 코드가 2이며 수정 날짜가 기간에 속하며 지정된 경기와 일치하는 경기 수를 계산한다
     * request: 해당 경기와 기간을 가져온다
     * response: 검증을 통해 해당 기간의 확정경기와 취소 경기 수를 뽑는다
     */
    @Transactional
    private long calculateGameCount(LocalDate startDate, LocalDate endDate, List<Game> dailyGames, int matchStatus) {

        return dailyGames.stream()
                .filter(game -> game.getDeleteCode() == 2 && game.getModifiedAt() != null &&
                        game.getModifiedAt().isAfter(startDate.atStartOfDay()) &&
                        game.getModifiedAt().isBefore(endDate.atTime(LocalTime.MAX)) &&
                        game.getMatchStatus() == matchStatus)
                .count();
    }


    /**
     * 현재 달의 일별 통계를 계산
     * 해당 날짜의 게임 목록을 가져오고,
     * 그 후에 dayStats를 사용하여 해당 날짜에 대한 통계를 계산하고
     * monthDayStats 맵에 추가
     * request: 특정 날짜를 가져온다
     * response:날짜에 대한 계산된 모든 통계를 반환한다
     */
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

        log.info(monthDayStats + "일별 데이터");

        return monthDayStats;
    }

    /**
     * 총 회원자 수 구하는 로직
     * 회원이 추가 될 떄마다 바로 +1
     * @return 회원 수 DTO에 담아 반환
     */
    public UserCountDTO getTotalUserCount() {
        long totalUserCount = userManagementRepository.findAll().stream().count();
        return UserCountDTO.builder()
                .totalUserCount(totalUserCount)
                .build();
    }

    /**
     * 신규 회원자 수 구하는 로직
     * 하루 동안 사용자가 회원가입을 하면 +1
     * @return 신규 회원 수를 DTO에 담아 반환
     */
    public UserCountDTO getNewUserCount() {
        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1);

        List<User> newUsers = userManagementRepository.findByCreatedAtBetween(yesterday.atStartOfDay(), today.atStartOfDay());
        long newUserCount = newUsers.size();

        return UserCountDTO.builder()
                .newUserCount(newUserCount)
                .build();
    }

    /**
     * 신규 매치 글
     * 오늘 하루 동안 올라온 매치 글
     * @return 매치 글 갯수를 DTO에 담아서 전달
     */
    public StatsDTO getNewPostCount() {
        LocalDateTime today = LocalDateTime.now();
        LocalDateTime yesterday = today.minusDays(1);

        List<Game> newPosts = gameManagementRepository.findByCreatedAtBetween(yesterday, today);
        long newPostCount = newPosts.size();
        return StatsDTO.builder()
                .newPostCount(newPostCount)
                .build();
    }

    /**
     * 오늘 하루 동안 게임수 구하는 로직
     * matchStatus==1 이며 schedule이 오늘 하루동안 이뤄진 경기
     * @return
     */
    public StatsDTO getTodayGamesCount() {


        LocalDateTime now = LocalDateTime.now(); // 현재 날짜와 시간을 가져옵니다.

        List<Game> todayGames = gameManagementRepository.findAll().stream()
                .filter(game -> game.getMatchStatus() == 1 && game.getSchedule().toLocalDate().isEqual(now.toLocalDate()))
                .collect(Collectors.toList());
        long todayGameCount = (long) todayGames.size();

        return StatsDTO.builder()
                .todayGameCount(todayGameCount)
                .build();

    }

    /**
     * Admin Main페이지에 전달할 데이터들 map으로 묶기
     * @return 총 회원 수, 신규 회원 수 , 오늘 경기 수, 오늘 올라 온글
     */
    public Map<String, Object> getAdminStats() {
        UserCountDTO totalUserCount = getTotalUserCount();
        UserCountDTO newUserCount = getNewUserCount();
        StatsDTO todayGameCount = getTodayGamesCount();
        StatsDTO newPostCount = getNewPostCount();

        Map<String, Object> adminStatsMap = new HashMap<>();
        adminStatsMap.put("totalUserCount", totalUserCount);
        adminStatsMap.put("newUserCount", newUserCount);
        adminStatsMap.put("todayGameCount", todayGameCount);
        adminStatsMap.put("newPostCount", newPostCount);

        return adminStatsMap;
    }

}