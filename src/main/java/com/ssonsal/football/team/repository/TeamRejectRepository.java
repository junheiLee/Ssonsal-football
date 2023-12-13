package com.ssonsal.football.team.repository;


import com.ssonsal.football.team.entity.RejectId;
import com.ssonsal.football.team.entity.TeamReject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TeamRejectRepository extends JpaRepository<TeamReject, RejectId> {

    /**
     * 팀에 유저에 대한 거절/밴 기록이 있는지 확인한다.
     *
     * @param userId
     * @param teamId
     */
    boolean existsByRejectIdUserIdAndRejectIdTeamId(Long userId, Long teamId);

    List<TeamReject> findAllByRejectId_TeamId(Long teamId);

}
