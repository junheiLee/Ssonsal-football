package com.ssonsal.football.team.service;

import com.ssonsal.football.team.dto.request.TeamCreateDto;
import com.ssonsal.football.team.dto.request.TeamEditDto;
import com.ssonsal.football.team.dto.response.*;

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

    Long editTeam(TeamEditDto teamEditDto);

    Map<String, Object> createTeam(TeamCreateDto teamCreateDto, Long user);

    boolean checkNameDuplicate(String name, Long teamId);

    boolean checkNameDuplicate(String name);

    TeamEditFormDto findTeamInfo(Long teamId);

}
