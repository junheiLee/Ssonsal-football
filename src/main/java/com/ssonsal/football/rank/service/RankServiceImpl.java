package com.ssonsal.football.rank.service;

import com.ssonsal.football.rank.dto.RankListResponseDto;
import com.ssonsal.football.rank.dto.UpdatedRankDto;
import com.ssonsal.football.rank.entity.Rank;
import com.ssonsal.football.rank.repository.RankRepository;
import com.ssonsal.football.team.entity.TeamRecord;
import com.ssonsal.football.team.repository.TeamRecordRepository;
import com.ssonsal.football.team.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class RankServiceImpl implements RankService {

    private final RankRepository rankRepository;
    private final TeamRecordRepository teamRecordRepository;


    // 랭킹 리스트
    @Transactional
        @Override
        public List<RankListResponseDto> findRankList(Integer month) {

            List<TeamRecord> teamRecordsWithRanks = calculatePointsAndResetRanks(month);

        return teamRecordsWithRanks.stream()
                .map(rank -> new RankListResponseDto(rank.getTeam(), rank))
                .collect(Collectors.toList());
        }

        // 매달 1일 마다 랭킹 초기화
        @Override
        @Transactional
        public void resetRanks(Integer month) {
            LocalDateTime currentDate = LocalDateTime.now();
            if (currentDate.getDayOfMonth() == 1) {
                LocalDateTime startDate;
                LocalDateTime lastMonthStartDate;
                LocalDateTime lastMonthEndDate;

                    if (null != month) {
                        startDate = LocalDateTime.of(currentDate.getYear(), month, 1, 0, 0);
                        lastMonthStartDate = startDate.minusMonths(1);
                        lastMonthEndDate = startDate.minusDays(1);
                    } else {
                        startDate = currentDate.withDayOfMonth(1);
                        lastMonthStartDate = currentDate.minusMonths(1).withDayOfMonth(1);
                        lastMonthEndDate = startDate.minusDays(1);
                    }

                List<TeamRecord> lastMonthTeamRecords = teamRecordRepository.findByModifiedAtBetween(
                        lastMonthStartDate, lastMonthEndDate);

                for (TeamRecord teamRecord : lastMonthTeamRecords) {
                    RankListResponseDto rankListResponseDto = new RankListResponseDto(teamRecord.getTeam(), teamRecord);
                    rankListResponseDto.resetRank();
                }

            }
        }

        // 한달간 포인트 계산
    @Transactional
    public List<TeamRecord> calculatePointsAndResetRanks(Integer month) {
        LocalDateTime currentDate = LocalDateTime.now();
        int year = currentDate.getYear();
        int currentMonth;

        if (month != null) {
            currentMonth = month;
        } else {
            currentMonth = currentDate.getMonthValue();
        }

        LocalDateTime startDate = LocalDateTime.of(year, currentMonth, 1, 0, 0);
        LocalDateTime endDate = startDate.with(TemporalAdjusters.lastDayOfMonth()).withHour(23).withMinute(59).withSecond(59);
        List<TeamRecord> teamRecords = teamRecordRepository.findByModifiedAtBetween(startDate, endDate);

        Map<Long, Integer> teamRankMap = calculateTeamRanks(teamRecords);

        for (TeamRecord target : teamRecords) {
            rankRepository.save(
                    Rank.builder()
                            .team(target.getTeam())
                            .spot(teamRankMap.get(target.getTeam().getId()))
                            .build()
            );
        }

        return teamRecords;
    }

    @Transactional
    // spot(승점) 순위 정렬
    private Map<Long, Integer> calculateTeamRanks(List<TeamRecord> teamRecords) {
        teamRecords.sort(Comparator.comparingInt(TeamRecord::getPoint).reversed()
                .thenComparingInt(TeamRecord::getWinCount).reversed()
                .thenComparingInt(TeamRecord::getTotalGameCount).reversed()
                .thenComparing(Comparator.comparing(teamRecord -> teamRecord.getTeam().getCreatedAt())));

        Map<Long, Integer> teamRankMap = new HashMap<>();
        int currentSpot = 1;
        int currentPoints = Integer.MAX_VALUE;

        for (TeamRecord target : teamRecords) {
            if (target.getPoint() < currentPoints) {
                currentSpot++;
                currentPoints = target.getPoint();
            }
            teamRankMap.put(target.getTeam().getId(), currentSpot);
        }

        return teamRankMap;
    }




    @Override
    @Transactional
    public UpdatedRankDto updateRank() {
        List<TeamRecord> results = teamRecordRepository.determineRank();
        log.info("updateRank() 호출, results.size() = {}", results.size());

        saveRank(results);
        return new UpdatedRankDto(1L, LocalDateTime.now());
    }


    @Transactional
    private void saveRank(List<TeamRecord> results) {
        int spot = 1;

        for (TeamRecord target : results) {
            rankRepository.save(
                    Rank.builder()
                            .team(target.getTeam())
                            .spot(spot)
                            .build()
            );
            spot++;
        }
    }



}
