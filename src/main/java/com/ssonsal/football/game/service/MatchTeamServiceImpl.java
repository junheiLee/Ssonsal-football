package com.ssonsal.football.game.service;

import com.ssonsal.football.game.dto.request.AcceptTeamRequestDto;
import com.ssonsal.football.game.dto.response.GameResultResponseDto;
import com.ssonsal.football.game.dto.response.MatchTeamResponseDto;
import com.ssonsal.football.game.entity.Game;
import com.ssonsal.football.game.entity.MatchApplication;
import com.ssonsal.football.game.repository.MatchApplicationRepository;
import com.ssonsal.football.game.repository.SubRepository;
import com.ssonsal.football.game.util.GameResultSet;
import com.ssonsal.football.game.util.TeamResult;
import com.ssonsal.football.global.exception.CustomException;
import com.ssonsal.football.team.entity.Team;
import com.ssonsal.football.team.entity.TeamRecord;
import com.ssonsal.football.team.repository.TeamRecordRepository;
import com.ssonsal.football.user.entity.User;
import com.ssonsal.football.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.ssonsal.football.game.entity.MatchStatus.WAITING;
import static com.ssonsal.football.game.exception.GameErrorCode.*;
import static com.ssonsal.football.game.util.GameConstant.*;
import static com.ssonsal.football.game.util.TeamResult.END;
import static com.ssonsal.football.game.util.TeamResult.peekResult;
import static com.ssonsal.football.global.util.ErrorCode.USER_NOT_FOUND;
import static com.ssonsal.football.global.util.transfer.Transfer.longIdToMap;


@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class MatchTeamServiceImpl implements MatchTeamService {

    private final UserRepository userRepository;
    private final MatchApplicationRepository matchApplicationRepository;
    private final TeamRecordRepository teamRecordRepository;
    private final SubRepository subRepository;

    @Override
    public MatchTeamResponseDto findMatchTeamInfo(Long matchTeamId) {


        MatchTeamResponseDto matchTeam = matchApplicationRepository.searchMatchTeamDto(matchTeamId);
        validateIsExistMatchTeam(matchTeam);

        matchTeam.countHavingSub(subRepository.countByTeamIdAndGameId(matchTeam.getTeamId(), matchTeam.getGameId()));

        return matchTeam;
    }

    private void validateIsExistMatchTeam(MatchTeamResponseDto matchTeam) {
        if (matchTeam == null) {
            throw new CustomException(NOT_EXIST_APPLICATION);
        }
    }

    @Override
    @Transactional
    public Long acceptAwayTeam(Long loginUserId, AcceptTeamRequestDto approvalAwayTeamDto) {

        User loginUser = getUser(loginUserId);

        Long matchApplicationId = approvalAwayTeamDto.getMatchApplicationId();
        MatchApplication targetApplication = getMatchApplication(matchApplicationId);

        Game game = targetApplication.getGame();

        validateUserInTargetTeam(game.getHome(), loginUser.getTeam());
        validateGameIsWaiting(game);
        validateIsNotHome(game, targetApplication.getTeam());

        game.changeRemainApplicationsStatus();// 모든 Applicant Status 대기 -> 보류로 변경
        targetApplication.approve();

        return game.getId();
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

    private void validateIsNotHome(Game game, Team team) {
        if (game.getHome().equals(team)) {
            log.error("home 은 신청한 팀이 아니므로 승인할 수 없음.");
            throw new CustomException(NOT_EXIST_APPLICATION);
        }
    }

    @Override
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
            return gameResultResponseDto.calTotalScore(homeScore);
        }

        TeamResult awayResult = peekResult(awayResultInKo);
        Integer totalScore = homeScore + awayResult.getScore();

        // 양 팀 모두 옳은 승패를 입력한 경우 team_record 테이블에 결과 기입
        if (totalScore.equals(END.getScore())) {

            enterResult(game, homeResult, awayResult);
            return gameResultResponseDto.calTotalScore(totalScore);
        }

        // 승-패, 패-승, 무-무 외의 입력은 양팀에서 입력한 결과를 초기화 시킨다.
        initResult(game);
        return GameResultResponseDto.builder().build();
    }

    @Override
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
            return gameResultResponseDto.calTotalScore(awayScore);
        }

        TeamResult homeResult = peekResult(homeResultInKo);
        Integer totalScore = homeResult.getScore() + awayScore;

        // 양 팀 모두 옳은 승패를 입력한 경우
        if (totalScore.equals(END.getScore())) {

            enterResult(game, homeResult, awayResult);
            return gameResultResponseDto.calTotalScore(totalScore);
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

    private void initResult(Game game) {
        game.enterHomeTeamResult(null);
        game.enterAwayTeamResult(null);
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND, longIdToMap(USER_ID, userId)));
    }

    private MatchApplication getMatchApplication(Long matchApplicationId) {
        return matchApplicationRepository.findById(matchApplicationId)
                .orElseThrow(() -> new CustomException(NOT_EXIST_APPLICATION,
                        longIdToMap(MATCH_APPLICATION_ID, matchApplicationId)));
    }

    private TeamRecord getTeamRecord(Long teamId) {
        return teamRecordRepository.findById(teamId)
                .orElseThrow(() -> new CustomException(NOT_EXIST_TEAM, longIdToMap(TEAM_RECORD_ID, teamId)));
    }

}
