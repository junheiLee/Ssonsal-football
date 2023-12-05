package com.ssonsal.football.game.service;

import com.ssonsal.football.game.dto.request.ApprovalTeamRequestDto;
import com.ssonsal.football.game.entity.ApplicantStatus;
import com.ssonsal.football.game.entity.Game;
import com.ssonsal.football.game.entity.MatchApplication;
import com.ssonsal.football.game.entity.MatchStatus;
import com.ssonsal.football.game.exception.GameErrorCode;
import com.ssonsal.football.game.exception.MatchErrorCode;
import com.ssonsal.football.game.repository.GameRepository;
import com.ssonsal.football.global.exception.CustomException;
import com.ssonsal.football.global.util.ErrorCode;
import com.ssonsal.football.user.entity.User;
import com.ssonsal.football.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class MatchTeamServiceImpl implements MatchTeamService {

    private final GameRepository gameRepository;
    private final UserRepository userRepository;


    @Transactional
    public Long approvalAwayTeam(Long userId, Long gameId,
                                 ApprovalTeamRequestDto approvalAwayTeamDto) {

        // 해당 게임
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_EXIST));
        checkGameIsWaiting(game.getMatchStatus());

        // 요청을 보낸 user 가 게임을 생성한 팀의 팀원인지 확인
        User user = userRepository.findById(userId).get();
        checkUserInHomeTeam(game, user);

        // Applicant Status 대기 -> 보류로 변경
        List<MatchApplication> applicantsTeams = game.getMatchApplications();
        applicantsTeams.stream()
                .filter(team -> team.getApplicationStatus().equals(ApplicantStatus.WAITING.getDescription()))
                .forEach(MatchApplication::changeStatusToWaiting);

        // 승인할 팀 신청
        Long teamId = approvalAwayTeamDto.getTeamId();
        MatchApplication applicantTeam = applicantsTeams.stream().filter(target -> Objects.equals(target.getTeam().getId(), teamId))
                .findAny()
                .orElseThrow(() -> new CustomException(MatchErrorCode.NOT_APPLICANT_TEAM));

        // 승인 로직
        applicantTeam.approval();

        return game.getId();
    }

    private void checkUserInHomeTeam(Game game, User user) {
        if (!user.getTeam().equals(game.getHometeam())) {
            throw new CustomException(GameErrorCode.NOT_HOMETEAM_MEMBER);
        }
    }

    private void checkGameIsWaiting(int gameMatchStatus) {
        int waitingStatus = MatchStatus.WAITING.getCodeNumber();

        if (!(gameMatchStatus == waitingStatus)) {
            throw new CustomException(GameErrorCode.ALREADY_CONFIRMED_GAME);
        }
    }
}
