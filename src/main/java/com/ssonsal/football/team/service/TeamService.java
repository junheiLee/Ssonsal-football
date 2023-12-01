package com.ssonsal.football.team.service;

import com.ssonsal.football.team.dto.response.TeamApplyDto;
import com.ssonsal.football.team.dto.response.TeamDetailDto;
import com.ssonsal.football.team.dto.response.TeamListDto;
import com.ssonsal.football.team.dto.response.TeamMemberListDto;

import java.util.List;
import java.util.Map;

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
    Integer findRanking(Long teamId);

    /**
     * 모집 중인 팀만 내림차순으로 정렬해서 가져온다.
     *
     * @return 모집 중인 팀 목록
     */
    List<TeamListDto> findRecruitList();

    /**
     * 검색한 팀명에 맞는 팀을 가져온다.
     *
     * @param keyword 검색어
     * @return 검색한 팀 목록
     */
    List<TeamListDto> searchName(String keyword);

    /**
     * 팀 상세정보를 가져온다.
     *
     * @param teamId 팀 아이디
     * @return 팀 상세정보
     */
    TeamDetailDto findDetail(Long teamId);

    /**
     * 팀의 회원목록을 가져온다.
     *
     * @param teamId 팀 아이디
     * @return 팀 회원 정보 목록
     */
    List<TeamMemberListDto> findMemberList(Long teamId);

    /**
     * 팀장명을 가져온다.
     *
     * @param teamId 팀 아이디
     * @return 팀장 닉네임
     */
    String findLeader(Long teamId);

    /**
     * 팀원정보와 팀 신청목록을 가져온다.
     *
     * @param teamId 팀 아이디
     * @return map 관리 대상 목록이 담긴 map
     */
    Map<String, Object> findManageList(Long teamId);

    /**
     * 팀에 신청한 유저 목록을 가져온다.
     *
     * @param teamId 팀 id
     * @return 신청한 유저 정보 목록
     */
    List<TeamApplyDto> findApplyList(Long teamId);

}
