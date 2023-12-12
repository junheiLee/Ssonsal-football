package com.ssonsal.football.game.service;

import com.ssonsal.football.game.dto.request.ApprovalTeamRequestDto;
import com.ssonsal.football.game.dto.response.GameResultResponseDto;
import com.ssonsal.football.game.dto.response.MatchTeamResponseDto;
import com.ssonsal.football.game.entity.Game;
import com.ssonsal.football.game.entity.MatchApplication;
import com.ssonsal.football.game.repository.GameRepository;
import com.ssonsal.football.game.repository.MatchApplicationRepository;
import com.ssonsal.football.game.repository.SubRepository;
import com.ssonsal.football.game.util.GameResultSet;
import com.ssonsal.football.game.util.TeamResult;
import com.ssonsal.football.global.exception.CustomException;
import com.ssonsal.football.team.entity.Team;
import com.ssonsal.football.team.entity.TeamRecord;
import com.ssonsal.football.team.repository.TeamRecordRepository;
import com.ssonsal.football.team.repository.TeamRepository;
import com.ssonsal.football.user.entity.User;
import com.ssonsal.football.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.ssonsal.football.game.entity.MatchStatus.WAITING;
import static com.ssonsal.football.game.exception.GameErrorCode.*;
import static com.ssonsal.football.game.util.GameConstant.*;
import static com.ssonsal.football.game.util.TeamResult.END;
import static com.ssonsal.football.game.util.TeamResult.peekResult;
import static com.ssonsal.football.game.util.Transfer.longIdToMap;
import static com.ssonsal.football.global.util.ErrorCode.USER_NOT_FOUND;


@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class MatchTeamServiceImpl implements MatchTeamService {

    private final UserRepository userRepository;
    private final GameRepository gameRepository;
    private final MatchApplicationRepository matchApplicationRepository;
    private final TeamRecordRepository teamRecordRepository;
    private final TeamRepository teamRepository;
    private final SubRepository subRepository;

    @Override
    public MatchTeamResponseDto getMatchTeam(Long teamId, Long gameId) {

        Team team = getTeam(teamId);
        Game game = getGame(gameId);

        List<MatchTeamResponseDto> matchTeam = matchApplicationRepository.searchMatchTeamDto(teamId, gameId);
        validateIsExistMatchTeam(matchTeam.size());

        matchTeam.get(0).isHavingSub(subRepository.existsByTeamAndGame(team, game));

        return matchTeam.get(0);
    }

    private void validateIsExistMatchTeam(int matchTeamSize) {
        if (matchTeamSize != 1) {
            throw new CustomException(NOT_EXIST_APPLICATION);
        }
    }

    private Team getTeam(Long teamId) {
        return teamRepository.findById(teamId)
                .orElseThrow(() -> new CustomException(NOT_EXIST_TEAM, longIdToMap(TEAM_ID, teamId)));
    }

    @Transactional
    public Long approveAwayTeam(Long userId, Long gameId,
                                ApprovalTeamRequestDto approvalAwayTeamDto) {

        User user = getUser(userId);
        Game game = getGame(gameId);
        Long approvalTeamId = approvalAwayTeamDto.getTeamId();

        validateUserInTargetTeam(game.getHome(), user.getTeam());
        validateGameIsWaiting(game);
        validateIsNotHome(game, approvalTeamId);

        game.changeRemainApplicationsStatus();// 모든 Applicant Status 대기 -> 보류로 변경

        MatchApplication targetApplication
                = matchApplicationRepository.findByTeamIdAndGameId(approvalTeamId, gameId)
                .orElseThrow(() -> new CustomException(NOT_EXIST_APPLICATION, longIdToMap(TEAM_ID, approvalTeamId)));
        targetApplication.approve();

        return game.getId();
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND, longIdToMap(USER_ID, userId)));
    }

    private Game getGame(Long gameId) {
        return gameRepository.findByIdAndDeleteCodeIs(gameId, NOT_DELETED)
                .orElseThrow(() -> new CustomException(NOT_EXIST_GAME, longIdToMap(GAME_ID, gameId)));
    }

    private void validateUserInTargetTeam(Team targetTeam, Team userTeam) {

        if (!targetTeam.equals(userTeam)) {
            log.error("user 가 접근하려는 Team 의 팀원이 아님.");
            throw new CustomException(NOT_IN_TARGET_TEAM, longIdToMap(TEAM_ID, targetTeam.getId()));
        }
    }

    private void validateGameIsWaiting(Game game) {

        if (game.getMatchStatus() != WAITING.getCodeNumber()) {
            throw new CustomException(NOT_WAITING_GAME, longIdToMap(GAME_ID, game.getId()));
        }
    }

    private void validateIsNotHome(Game game, Long teamId) {
        if (game.getHome().getId().equals(teamId)) {
            log.error("home 은 신청한 팀이 아니므로 승인할 수 없음.");
            throw new CustomException(NOT_EXIST_APPLICATION);
        }
    }

    @Transactional
    public GameResultResponseDto enterHomeTeamResult(Game game, TeamResult homeResult) {

        String awayResultInKo = game.getAwayteamResult();
        Integer homeScore = homeResult.getScore();

        game.enterHomeTeamResult(homeResult.getKo());
        GameResultResponseDto gameResultResponseDto = GameResultResponseDto.builder()
                .homeResult(homeResult.getKo())
                .awayResult(awayResultInKo)
                .build();

        // 한 팀만 승패를 입력한 경우
        if (awayResultInKo == null) {
            return gameResultResponseDto.setTotalScore(homeScore);
        }

        TeamResult awayResult = peekResult(awayResultInKo);
        Integer totalScore = homeScore + awayResult.getScore();

        // 양 팀 모두 옳은 승패를 입력한 경우 team_record 테이블에 결과 기입
        if (totalScore.equals(END.getScore())) {

            enterResult(game, homeResult, awayResult);
            return gameResultResponseDto.setTotalScore(totalScore);
        }

        // 승-패, 패-승, 무-무 외의 입력은 양팀에서 입력한 결과를 초기화 시킨다.
        initResult(game);
        return GameResultResponseDto.builder().build();
    }

    @Transactional
    public GameResultResponseDto enterAwayTeamResult(Game game, TeamResult awayResult) {
        String homeResultInKo = game.getHometeamResult();
        Integer awayScore = awayResult.getScore();

        game.enterAwayTeamResult(awayResult.getKo());
        GameResultResponseDto gameResultResponseDto = GameResultResponseDto.builder()
                .homeResult(homeResultInKo)
                .awayResult(awayResult.getKo())
                .build();

        // 한 팀만 승패를 입력한 경우
        if (homeResultInKo == null) {
            return gameResultResponseDto.setTotalScore(awayScore);
        }

        TeamResult homeResult = peekResult(homeResultInKo);
        Integer totalScore = homeResult.getScore() + awayScore;

        // 양 팀 모두 옳은 승패를 입력한 경우
        if (totalScore.equals(END.getScore())) {

            enterResult(game, homeResult, awayResult);
            return gameResultResponseDto.setTotalScore(totalScore);
        }

        // 승-패, 패-승, 무-무 외의 입력은 양팀에서 입력한 결과를 초기화 시킨다.
        initResult(game);
        return GameResultResponseDto.builder().build();
    }

    private void enterResult(Game game, TeamResult homeResult, TeamResult awayResult) {
        Long homeId = game.getHome().getId();
        Long awayId = game.getAway().getId();

        TeamRecord homeRecord = getTeamRecord(homeId);
        GameResultSet.getHomeRecordEntity(homeRecord, homeResult, awayResult);

        TeamRecord awayRecord = getTeamRecord(awayId);
        GameResultSet.getAwayRecordEntity(awayRecord, homeResult, awayResult);

        game.end();
    }

    private TeamRecord getTeamRecord(Long teamId) {
        return teamRecordRepository.findById(teamId)
                .orElseThrow(() -> new CustomException(NOT_EXIST_TEAM, longIdToMap(TEAM_RECORD_ID, teamId)));
    }

    private void initResult(Game game) {
        game.enterHomeTeamResult(null);
        game.enterAwayTeamResult(null);
    }

}
