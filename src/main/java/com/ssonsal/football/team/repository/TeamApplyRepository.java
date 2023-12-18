package com.ssonsal.football.team.repository;

import com.ssonsal.football.team.entity.TeamApply;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TeamApplyRepository extends JpaRepository<TeamApply, Long> {


    /**
     * 특정 팀의 모든 신청을 가져온다.
     *
     * @param teamId
     * @return 신청 정보
     */
    List<TeamApply> findAllByTeamId(Long teamId);

    /**
     * 유저가 해당 팀에 가입신청 중 인지 확인한다.
     *
     * @param userId 유저 아이디
     * @param teamId 팀 아이디
     */
    boolean existsByIdAndTeamId(Long userId, Long teamId);
}
