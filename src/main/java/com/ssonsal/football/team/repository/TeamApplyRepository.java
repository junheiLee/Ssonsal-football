package com.ssonsal.football.team.repository;

import com.ssonsal.football.team.dto.response.TeamApplyDto;
import com.ssonsal.football.team.entity.TeamApply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TeamApplyRepository extends JpaRepository<TeamApply, Long> {


    /**
     * 팀에 신청중인 유저 정보를 가져온다.
     *
     * @param teamId 팀 아이디
     * @return 신청한 유저 정보
     */
    @Query("SELECT new com.ssonsal.football.team.dto.response.TeamApplyDto(t.id, u.nickname, u.gender, u.position) FROM TeamApply t JOIN User u ON t.user.id = u.id WHERE t.team.id = :teamId")
    List<TeamApplyDto> findAllByTeamId(@Param("teamId") Long teamId);

    /**
     * 유저가 해당 팀에 가입신청 중 인지 확인한다.
     *
     * @param userId 유저 아이디
     * @param teamId 팀 아이디
     */
    boolean existsByIdAndTeamId(Long userId, Long teamId);
}
