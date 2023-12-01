package com.ssonsal.football.team.service;

import com.ssonsal.football.team.repository.TeamRejectRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class TeamRejectServiceImpl implements TeamRejectService {

    private final TeamRejectRepository teamRejectRepository;

    /**
     * 팀에대한 유저의 밴/거절 기록이 있는지 확인한다.
     *
     * @param userId 유저 아이디
     * @param teamId 팀 아이디
     */
    @Override
    public boolean isUserRejected(Long userId, Long teamId) {

        return teamRejectRepository.existsByRejectIdUserIdAndRejectIdTeamId(userId, teamId);
    }

}
