package com.ssonsal.football.team.service;

import com.ssonsal.football.team.entity.Role;
import com.ssonsal.football.team.repository.TeamApplyRepository;
import com.ssonsal.football.team.repository.TeamRejectRepository;
import com.ssonsal.football.team.repository.TeamRepository;
import com.ssonsal.football.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class MemberServiceImpl implements MemberService {

    private final TeamRepository teamRepository;
    private final UserRepository userRepository;
    private final TeamApplyRepository teamApplyRepository;
    private final TeamRejectRepository teamRejectRepository;

    /**
     * 유저의 현재 직책에 따라 다른 값을 넘겨준다.
     *
     * @param userId 유저 아이디
     * @return 유저 권한
     */
    @Override
    public Role isUserLevel(Long userId) {

        if (isUserOtherApply(userId)) {
            return Role.TEAM_APPLY;
        } else if (isUserTeamExists(userId)) {
            return Role.USER;
        }

        return Role.TEAM_MEMBER;
    }

    /**
     * 유저가 특정 팀에 신청 중 인지 확인합니다.
     *
     * @param userId 유저 아이디
     */
    @Override
    public boolean isUserOtherApply(Long userId) {

        return teamApplyRepository.existsById(userId);
    }

    /**
     * 유저가 팀을 가지고 있는지 확인합니다. -> null 일때 true
     *
     * @param userId 유저 아이디
     */
    @Override
    public boolean isUserTeamExists(Long userId) {

        return userRepository.existsByIdAndTeamIsNull(userId);
    }

}
