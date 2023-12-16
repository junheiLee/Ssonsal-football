package com.ssonsal.football.rank.service;

import com.ssonsal.football.rank.dto.RankListResponseDto;
import com.ssonsal.football.rank.dto.UpdatedRankDto;

import java.util.List;

public interface RankService {

    UpdatedRankDto updateRank();

    List<RankListResponseDto> findRankList();
}
