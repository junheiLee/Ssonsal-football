package com.ssonsal.football.team.service;

import com.ssonsal.football.global.exception.CustomException;
import com.ssonsal.football.team.entity.RejectId;
import com.ssonsal.football.team.entity.Team;
import com.ssonsal.football.team.entity.TeamApply;
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
    public void createUserApply(Long userId, Long teamId) {

        User user = userRepository.findById(userId).orElseThrow(
                () -> new CustomException(TeamErrorCode.USER_NOT_FOUND)
        );
        Team team = teamRepository.findById(teamId).orElseThrow(
                () -> new CustomException(TeamErrorCode.TEAM_NOT_FOUND)
        );

        TeamApply teamApply = new TeamApply(user, team);

        teamApplyRepository.save(teamApply);
    }

    /**
     * 팀 가입 신청을 취소합니다.
     *
     * @param userId
     */
    @Override
    public void deleteUserApply(Long userId) {

        teamApplyRepository.deleteById(userId);
    }

    /**
     * 유저의 신청을 수락합니다.
     *
     * @param userId
     * @param teamId
     * @return 수락한 유저 닉네임
     */
    @Override
    public String userApplyAccept(Long userId, Long teamId) {

        deleteUserApply(userId);

        User user = userRepository.findById(userId).get();
        user.setTeam(teamRepository.findById(teamId).get());

        return user.getNickname();
    }

    /**
     * 유저의 신청을 거절합니다.
     *
     * @param userId
     * @param teamId
     * @return 거절한 유저 닉네임
     */
    @Override
    public String userApplyReject(Long userId, Long teamId) {

        User user = userRepository.findById(userId).orElseThrow(
                () -> new CustomException(TeamErrorCode.USER_NOT_FOUND));

        RejectId rejectId = new RejectId(user, teamId);
        TeamReject teamReject = new TeamReject(rejectId);

        teamRejectRepository.save(teamReject);
        deleteUserApply(userId);

        return user.getNickname();
    }


}
