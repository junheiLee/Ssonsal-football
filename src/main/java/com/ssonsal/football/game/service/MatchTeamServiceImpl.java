package com.ssonsal.football.game.service;

import com.ssonsal.football.game.dto.request.ApprovalTeamRequestDto;
import com.ssonsal.football.game.dto.response.GameResultResponseDto;
import com.ssonsal.football.game.entity.ApplicantStatus;
import com.ssonsal.football.game.entity.Game;
import com.ssonsal.football.game.entity.MatchApplication;
import com.ssonsal.football.game.entity.MatchStatus;
import com.ssonsal.football.game.exception.GameErrorCode;
import com.ssonsal.football.game.exception.MatchErrorCode;
import com.ssonsal.football.game.repository.GameRepository;
import com.ssonsal.football.game.util.GameResult;
import com.ssonsal.football.game.util.Transfer;
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
        checkIsAwayTeam(game, teamId);
        MatchApplication applicantTeam = applicantsTeams.stream().filter(target -> Objects.equals(target.getTeam().getId(), teamId))
                .findAny()
                .orElseThrow(() -> new CustomException(MatchErrorCode.NOT_APPLICANT_TEAM, Transfer.dataToMap("teamId", teamId)));

        // 승인 로직
        applicantTeam.approval();

        return game.getId();
    }

    @Transactional
    public GameResultResponseDto enterAwayTeamResult(Game game, Integer resultScore) {
        Integer homeTeamResult = game.getHometeamResult();

        if (homeTeamResult == null) {
            game.enterAwayTeamResult(resultScore);
            return GameResultResponseDto.builder()
                    .homeTeamResult(homeTeamResult)
                    .awayTeamResult(resultScore)
                    .totalResult(resultScore)
                    .build();
        }

        Integer totalResult = homeTeamResult + resultScore;

        if (totalResult.equals(GameResult.END.getScore())) {
            game.enterAwayTeamResult(resultScore);
            return GameResultResponseDto.builder()
                    .homeTeamResult(homeTeamResult)
                    .awayTeamResult(resultScore)
                    .totalResult(totalResult)
                    .build();
        }

        // 홈팀에서 입력한 결과도 초기화 시킨다.
        game.enterHomeTeamResult(null);
        return GameResultResponseDto.builder().build();
    }

    @Transactional
    public GameResultResponseDto enterHomeTeamResult(Game game, Integer resultScore) {

        Integer awayTeamResult = game.getAwayteamResult();

        if (awayTeamResult == null) {

            game.enterHomeTeamResult(resultScore);
            return GameResultResponseDto.builder()
                    .homeTeamResult(resultScore)
                    .awayTeamResult(awayTeamResult)
                    .totalResult(resultScore)
                    .build();
        }

        Integer totalResult = awayTeamResult + resultScore;

        if (totalResult.equals(GameResult.END.getScore())) {
            game.enterHomeTeamResult(resultScore);
            return GameResultResponseDto.builder()
                    .homeTeamResult(resultScore)
                    .awayTeamResult(awayTeamResult)
                    .totalResult(totalResult)
                    .build();
        }

        // awayTeam 에서 입력한 결과도 초기화 시킨다.
        game.enterAwayTeamResult(null);
        return GameResultResponseDto.builder().build();
    }

    private void checkIsAwayTeam(Game game, Long teamId) {
        if (game.getHome().getId().equals(teamId)) {
            throw new CustomException(MatchErrorCode.NOT_APPLICANT_TEAM);
        }
    }

    private void checkUserInHomeTeam(Game game, User user) {
        if (!user.getTeam().equals(game.getHome())) {
            throw new CustomException(MatchErrorCode.NOT_TEAM_MEMBER, Transfer.dataToMap("userId",user.getId()));
        }
    }

    private void checkGameIsWaiting(int gameMatchStatus) {
        int waitingStatus = MatchStatus.WAITING.getCodeNumber();

        if (!(gameMatchStatus == waitingStatus)) {
            throw new CustomException(GameErrorCode.ALREADY_CONFIRMED_GAME);
        }
    }
}
