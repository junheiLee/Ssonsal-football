package com.ssonsal.football.team.repository;

import com.ssonsal.football.team.dto.response.TeamDetailDto;
import com.ssonsal.football.team.dto.response.TeamListDto;
import com.ssonsal.football.team.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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

    /**
     * 검색한 팀명에 맞는 팀을 가져온다.
     *
     * @param keyword
     * @return 검색된 팀 목록
     */
    @Query("SELECT new com.ssonsal.football.team.dto.response.TeamListDto(t.id, t.name, t.preferredArea, t.skillScore) FROM Team t WHERE t.name LIKE %:keyword%")
    List<TeamListDto> findByNameContaining(@Param("keyword") String keyword);

    /**
     * 팀 상세정보를 팀 전적과 함께 가져온다.
     *
     * @param teamId
     * @return 팀 상세정보
     */
    @Query("SELECT new com.ssonsal.football.team.dto.response.TeamDetailDto(t.id, t.name, t.preferredArea, t.preferredTime, t.intro, " +
            "tr.winCount, tr.drawCount, tr.loseCount, t.mannerScore, t.skillScore) " +
            "FROM Team t " +
            "JOIN TeamRecord tr ON t.id = tr.team.id " +
            "WHERE t.id = :teamId")
    TeamDetailDto findTeamDtoWithRecord(@Param("teamId") Long teamId);

    /**
     * 유저가 팀장인지 확인한다.
     *
     * @param teamId
     * @param userId
     */
    boolean existsByIdAndLeaderId(Long teamId, Long userId);
}
