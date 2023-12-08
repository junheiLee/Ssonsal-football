package com.ssonsal.football.team.service;

import com.ssonsal.football.global.exception.CustomException;
import com.ssonsal.football.team.entity.RejectId;
import com.ssonsal.football.team.entity.Role;
import com.ssonsal.football.team.entity.Team;
import com.ssonsal.football.team.entity.TeamReject;
import com.ssonsal.football.team.exception.TeamErrorCode;
import com.ssonsal.football.team.repository.TeamApplyRepository;
import com.ssonsal.football.team.repository.TeamRejectRepository;
import com.ssonsal.football.team.repository.TeamRepository;
import com.ssonsal.football.user.entity.User;
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
    private final UserRepository userRepository;

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
        } else if (hasAnyTeam(userId)) {
            return Role.TEAM_MEMBER;
        }

        return Role.USER;
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
        } else if (!hasAnyTeam(userId)) {
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
     * 유저가 팀을 가지고 있는지 확인합니다.
     *
     * @param userId 유저 아이디
     */
    @Override
    public boolean hasAnyTeam(Long userId) {

        return teamRepository.existsByUsers_IdAndUsers_TeamIsNotNull(userId);
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

    /**
     * 유저의 팀 정보를 삭제합니다.
     *
     * @param userId 유저 아이디
     * @param teamId 팀 아이디
     * @return 탈퇴하는 팀명
     */
    @Override
    @Transactional
    public String userLeaveTeam(Long teamId, Long userId) {

        User user = userRepository.findById(userId).orElseThrow(
                () -> new CustomException(TeamErrorCode.USER_NOT_FOUND));

        user.deleteTeam(user);

        Team team = teamRepository.findById(teamId).orElseThrow(
                () -> new CustomException(TeamErrorCode.TEAM_NOT_FOUND));

        return team.getName();
    }

    /**
     * 팀장을 변경합니다.
     *
     * @param teamId 팀 아이디
     * @param userId 위임받을 사람 아이디
     * @return 위임받은 사람 닉네임
     */
    @Override
    @Transactional
    public String leaderDelegate(Long teamId, Long userId) {

        Team team = teamRepository.findById(teamId).orElseThrow(
                () -> new CustomException(TeamErrorCode.TEAM_NOT_FOUND));

        team.delegateLeader(userId);

        return userRepository.findById(userId).get().getNickname();
    }

    /**
     * 팀에서 회원을 퇴출시킨다.
     *
     * @param userId 퇴출자 아이디
     * @param teamId 팀 아이디
     * @return 퇴출당한 회원 닉네임
     */
    @Override
    @Transactional
    public String userBan(Long userId, Long teamId) {

        User user = userRepository.findById(userId).orElseThrow(
                () -> new CustomException(TeamErrorCode.USER_NOT_FOUND));

        user.deleteTeam(user);

        RejectId rejectId = new RejectId(user, teamId);
        TeamReject teamReject = new TeamReject(rejectId);

        teamRejectRepository.save(teamReject);

        return user.getNickname();
    }

    /**
     * 특정 유저의 밴을 해제한다.
     *
     * @param teamId 팀 아이디
     * @param userId 대상 유저 아이디
     * @return 해제된 유저 닉네임
     */
    @Override
    @Transactional
    public String userBanCancel(Long teamId, Long userId) {

        User user = userRepository.findById(userId).orElseThrow(
                () -> new CustomException(TeamErrorCode.USER_NOT_FOUND));

        RejectId rejectId = new RejectId(user, teamId);
        TeamReject teamReject = new TeamReject(rejectId);
        teamRejectRepository.delete(teamReject);

        return user.getNickname();
    }

    /**
     * 팀이 모집중인지 확인한다.
     *
     * @param teamId 팀 아이디
     */
    @Override
    public boolean isTeamRecruit(Long teamId) {

        return teamRepository.existsByIdAndRecruit(teamId, 1);
    }
}
