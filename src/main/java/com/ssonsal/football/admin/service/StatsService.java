package com.ssonsal.football.admin.service;

import com.ssonsal.football.admin.dto.response.StatsDTO;
import com.ssonsal.football.game.entity.Game;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface StatsService {

    // =======================================일간 데이터 ===========================================

    /**
     * 확정된 경기와 취소된 경기 데이터를 DTO에 저장하는 로직
     * 전체
     * request: game.getMatchStatus의 0 또는 1 을 가져온다
     * response: statsDTO에 저장
     */
    StatsDTO statsDTO(List<Game> monthlyGames);

    /**
     * 날짜 데이터(한달 구하는 로직)
     * 계산된 확정 데이터와 취소 데이터의 범위를 설정해준다
     * 기준을 한달로 잡고 현재 날짜까지만 통계로 보여진다
     * request: game.getMatchStatus의 0 또는 1 을 가져온다
     * response: statsDTO에 저장
     */
    StatsDTO monthStats(LocalDate currentDate);


    // ======================================= 월간 데이터 ===========================================

    /**
     * 현재 달의 일별 통계를 계산
     * 해당 날짜의 게임 목록을 가져오고,
     * 그 후에 dayStats를 사용하여 해당 날짜에 대한 통계를 계산하고
     * monthDayStats 맵에 추가
     * request: 특정 날짜를 가져온다
     * response:날짜에 대한 계산된 모든 통계를 반환한다
     */
    Map<LocalDate, StatsDTO> monthlyDailyStats(LocalDate currentDate);

    /**
     * 월간 일별 데이터 DTO 생성
     * 제공된 게임 목록 dailyGames을 기반으로
     * 해당 날짜에 대한 확정된 경기 수 및 취소된 경기 수를 계산
     * request: 지정한 날짜를 가져온다
     * response: 지정된 날짜의 확정 경기와 취소 경기를 DTO에 담는다
     */
    StatsDTO dayStats(LocalDate date, List<Game> dailyGames);


    /**
     * 날짜가 지나면 deleteCode 변경
     * deleteCode를 변경시키면서  modifiedAt으로 변경된 시점의 데이터들을 뽑을 수 있다
     * 현재 날짜와 시간을 사용하여 날짜가 지나면 게임의 정보를 변경 시키는데
     * deleteCode를 2로 변경 시킨다.
     * <p>
     * request: 하루 데이터를 가져온다
     * response: 하루가 지나면 deleteCode는 2로 변경된다
     */
    void updateDeleteCodeForPastGames();

    /**
     * 하루의 게임 수 계산
     * 삭제 코드가 2이며 수정 날짜가 기간에 속하며 지정된 경기와 일치하는 경기 수를 계산한다
     * request: 해당 경기와 기간을 가져온다
     * response: 검증을 통해 해당 기간의 확정경기와 취소 경기 수를 뽑는다
     */
    long calculateGameCount(LocalDate startDate, LocalDate endDate, List<Game> dailyGames, int matchStatus);

    /**
     * 전체 회원 수 구하는 로직
     *
     * @return User테이블의 모든 id 값을 꺼내서 그 갯수를 반환
     */
    long getTotalUserCount();


    /**
     * 신규 회원자 수 구하는 로직
     * 하루 동안 사용자가 회원가입을 하면 +1
     *
     * @return 신규 회원 수를 반환
     */
    long getNewUserCount();

    /**
     * 신규 매치 글
     * 오늘 하루 동안 올라온 매치 글
     *
     * @return 매치 글 갯수를 반환
     */
    long getNewPostCount();

    /**
     * 오늘 하루 동안 게임수 구하는 로직
     * matchStatus==1 이며 schedule이 오늘 하루동안 이뤄진 경기
     *
     * @return 오늘 경기 수를 반환
     */
    long getTodayGamesCount();


    /**
     * Admin Main 페이지에 전달할 데이터들을 각 변수로 반환
     *
     * @return 총 회원 수, 신규 회원 수 , 오늘 경기 수, 오늘 올라 온 글
     */
    Map<String, Long> getAdminStats();
}

