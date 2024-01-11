package com.ssonsal.football.rank.service;

import com.ssonsal.football.rank.dto.MonthRequest;
import com.ssonsal.football.rank.dto.RankListResponseDto;
import com.ssonsal.football.rank.dto.UpdatedRankDto;
import com.ssonsal.football.team.entity.TeamRecord;

import java.util.List;

public interface RankService {

    List<RankListResponseDto> findRankList(Integer month);

    void resetRanks(Integer month);

    List<TeamRecord> calculatePointsAndResetRanks(Integer month);

    UpdatedRankDto updateRank();
}
