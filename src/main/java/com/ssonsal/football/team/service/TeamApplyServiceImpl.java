package com.ssonsal.football.team.service;

import com.ssonsal.football.global.exception.CustomException;
import com.ssonsal.football.global.util.ErrorCode;
import com.ssonsal.football.team.entity.RejectId;
import com.ssonsal.football.team.entity.Team;
import com.ssonsal.football.team.entity.TeamApply;
import com.ssonsal.football.team.entity.TeamReject;
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
@Transactional
@Service
public class TeamApplyServiceImpl implements TeamApplyService {

    private final TeamApplyRepository teamApplyRepository;
    private final UserRepository userRepository;
    private final TeamRepository teamRepository;
    private final TeamRejectRepository teamRejectRepository;

    /**
     * 팀에 신청을 넣습니다.
     *
     * @param userId 유저 아이디
     * @param teamId 팀 아이디
     */
    @Override
    public String createUserApply(Long userId, Long teamId) {

        User user = userRepository.findById(userId).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Team team = teamRepository.findById(teamId).orElseThrow(
                () -> new CustomException(ErrorCode.TEAM_NOT_FOUND));

        teamApplyRepository.save(new TeamApply(user, team));

        return team.getName();
    }

    /**
     * 팀 가입 신청을 취소합니다.
     *
     * @param userId 유저 아이디
     */
    @Override
    public void deleteUserApply(Long userId) {

        teamApplyRepository.deleteById(userId);
    }

    /**
     * 유저의 신청을 수락합니다.
     *
     * @param userId 유저 아이디
     * @param teamId 팀 아이디
     * @return 수락한 유저 닉네임
     */
    @Override
    public String userApplyAccept(Long userId, Long teamId) {

        User user = userRepository.findById(userId).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Team team = teamRepository.findById(teamId).orElseThrow(
                () -> new CustomException(ErrorCode.TEAM_NOT_FOUND));

        user.joinTeam(team);

        deleteUserApply(userId);

        return user.getNickname();
    }

    /**
     * 유저의 신청을 거절합니다.
     *
     * @param userId 유저 아이디
     * @param teamId 팀 아이디
     * @return 거절한 유저 닉네임
     */
    @Override
    public String userApplyReject(Long userId, Long teamId) {

        User user = userRepository.findById(userId).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND));

        RejectId rejectId = new RejectId(user, teamId);
        TeamReject teamReject = new TeamReject(rejectId);

        teamRejectRepository.save(teamReject);
        deleteUserApply(userId);

        return user.getNickname();
    }


}
