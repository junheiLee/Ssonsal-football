package com.ssonsal.football.team.repository;

import com.ssonsal.football.team.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TeamRepository extends JpaRepository<Team, Long> {

    /**
     * 모든 팀을 내림차순으로 가져온다.
     *
     * @return teamListDto 모든 팀 목록
     */
    List<Team> findAllByOrderByIdDesc();

    /**
     * 모집 중인 팀을 내림차순으로 가져온다.
     *
     * @param recruit 모집 중 인지 여부
     * @return 모집중인 팀 목록
     */
    List<Team> findAllByRecruitOrderByIdDesc(Integer recruit);

    /**
     * 검색한 팀명에 맞는 팀을 가져온다.
     *
     * @param keyword 팀명 검색어
     * @return 검색된 팀 목록
     */
    List<Team> findAllByNameContaining(String keyword);

    /**
     * 유저가 팀장인지 확인한다.
     *
     * @param teamId 팀 아이디
     * @param userId 유저 아이디
     */
    boolean existsByIdAndLeaderId(Long teamId, Long userId);

    /**
     * 유저가 팀을 가지고 있는지 확인한다.
     *
     * @param userId 유저 아이디
     */
    boolean existsByUsers_IdAndUsers_TeamIsNotNull(Long userId);

    /**
     * 유저가 특정 팀의 소속인지 확인한다.
     *
     * @param userId 유저 아이디
     * @param teamId 팀 아이디
     */
    boolean existsUsersByIdAndUsersId(Long teamId, Long userId);

    /**
     * 팀명이 중복인지 확인한다.
     *
     * @param name 팀 이름
     */
    boolean existsByName(String name);

    /**
     * 팀이 모집중인지 확인한다.
     *
     * @param teamId  팀 아이디
     * @param recruit 모집 여부
     */
    boolean existsByIdAndRecruit(Long teamId, Integer recruit);

}
