package com.ssonsal.football.team.service;

import com.ssonsal.football.team.dto.response.TeamApplyDto;
import com.ssonsal.football.team.dto.response.TeamDetailDto;
import com.ssonsal.football.team.dto.response.TeamListDto;
import com.ssonsal.football.team.dto.response.TeamMemberListDto;

import java.util.List;
import java.util.Map;

public interface TeamService {


    List<TeamListDto> findAllTeams();

    Integer findRank(Long teamId);

    List<TeamListDto> findRecruitList();

    List<TeamListDto> findSearchList(String keyword);

    TeamDetailDto findDetail(Long teamId);

    List<TeamMemberListDto> findMemberList(Long teamId);

    String findLeader(Long teamId);

    Map<String, Object> findManageList(Long teamId);

    List<TeamApplyDto> findApplyList(Long teamId);

    String findAgeAverage(Long teamId);

    public String findAgeGroup(char second);

}
