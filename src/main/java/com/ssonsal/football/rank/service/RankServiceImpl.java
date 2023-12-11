package com.ssonsal.football.rank.service;

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
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class RankServiceImpl implements RankService {

    private final RankRepository rankRepository;
    private final TeamRepository teamRepository;
    private final TeamRecordRepository teamRecordRepository;

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
            log.info("teamRecord.team={}", target.toString());
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
