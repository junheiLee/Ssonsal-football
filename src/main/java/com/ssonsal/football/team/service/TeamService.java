package com.ssonsal.football.team.service;

import com.ssonsal.football.team.dto.response.TeamListDto;

import java.util.List;

public interface TeamService {

    /**
     * 모든 팀을 내림차순으로 정렬해서 가져온다.
     *
     * @return teamListDto 전체 팀 목록
     */
    List<TeamListDto> findList();

    /**
     * 팀 랭킹을 구한다
     *
     * @param teamId 팀 아이디
     * @return 팀 랭킹값
     */
    Integer findTeamRanking(Long teamId);
}
