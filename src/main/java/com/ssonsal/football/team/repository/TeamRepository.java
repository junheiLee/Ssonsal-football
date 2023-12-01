package com.ssonsal.football.team.repository;

import com.ssonsal.football.team.dto.response.TeamListDto;
import com.ssonsal.football.team.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TeamRepository extends JpaRepository<Team, Long> {

    /**
     * 모든 팀을 내림차순으로 가져온다.
     *
     * @return teamListDto 모든 팀 목록
     */
    @Query("SELECT new com.ssonsal.football.team.dto.response.TeamListDto(t.id, t.name, t.preferredArea, t.skillScore) FROM Team t")
    List<TeamListDto> findAllByOrderByIdDesc();

    /**
     * 모집 중인 팀을 내림차순으로 가져온다.
     *
     * @param recruit 모집 중 인지 여부
     * @return 모집중인 팀 목록
     */
    @Query("SELECT new com.ssonsal.football.team.dto.response.TeamListDto(t.id, t.name, t.preferredArea, t.skillScore) FROM Team t WHERE t.recruit = ?1")
    List<TeamListDto> findAllByRecruitOrderByIdDesc(Integer recruit);

}
