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
        } else if (!isUserTeamExists(userId)) {
            return Role.USER;
        }

        return Role.TEAM_MEMBER;
    }

    /**
     * 유저의 현재 직책에 따라 다른 값을 넘겨준다.
     *
     * @param userId 유저 아이디
     * @param teamId 팀 아이디
     * @return 유저 권한
     */
    @Override
    public Role isUserLevel(Long teamId, Long userId) {

        if (isTeamLeader(teamId, userId)) {
            return Role.TEAM_LEADER;
        } else if (isUserTeamMember(teamId, userId)) {
            return Role.TEAM_MEMBER;
        } else if (isUserApply(userId, teamId)) {
            return Role.TEAM_APPLY;
        } else if (isUserOtherApply(userId)) {
            return Role.OTHER_TEAM_APPLY;
        } else if (!isUserTeamExists(userId)) {
            return Role.USER;
        }

        return Role.OTHER_TEAM_MEMBER;
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

        return teamRepository.existsByUserId(userId);
    }

    /**
     * 유저가 팀장인지 확인합니다.
     *
     * @param teamId 팀 아이디
     * @param userId 유저 아이디
     */
    @Override
    public boolean isTeamLeader(Long teamId, Long userId) {

        return teamRepository.existsByIdAndLeaderId(teamId, userId);
    }

    /**
     * 유저가 특정 팀의 소속인지 확인합니다.
     *
     * @param teamId 팀 아이디
     * @param userId 유저 아이디
     */
    @Override
    public boolean isUserTeamMember(Long teamId, Long userId) {
        System.out.println(teamRepository.existsUsersByIdAndUsersId(teamId, userId));
        return teamRepository.existsUsersByIdAndUsersId(teamId, userId);
    }

    /**
     * 유저가 해당 팀에 신청 중 인지 확인합니다.
     *
     * @param userId 유저 아이디
     * @param teamId 팀 아이디
     */
    @Override
    public boolean isUserApply(Long userId, Long teamId) {

        return teamApplyRepository.existsByIdAndTeamId(userId, teamId);
    }

}
