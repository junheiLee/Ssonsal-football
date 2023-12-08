package com.ssonsal.football.team.service;

import com.ssonsal.football.team.dto.response.TeamApplyDto;
import com.ssonsal.football.team.dto.response.TeamDetailDto;
import com.ssonsal.football.team.dto.response.TeamListDto;
import com.ssonsal.football.team.dto.response.TeamMemberListDto;

import java.util.List;
import java.util.Map;

public interface TeamService {


    List<TeamListDto> findAllTeams();

    List<TeamListDto> findRecruitList();

    List<TeamListDto> findSearchList(String keyword);

    TeamDetailDto findTeamDetail(Long teamId);

    List<TeamMemberListDto> findMemberList(Long teamId);

    String findLeaderName(Long teamId);

    Map<String, Object> findManageList(Long teamId);

    List<TeamApplyDto> findApplyList(Long teamId);

    String findAgeAverage(Long teamId);

    public String findAgeGroup(char second);


}
