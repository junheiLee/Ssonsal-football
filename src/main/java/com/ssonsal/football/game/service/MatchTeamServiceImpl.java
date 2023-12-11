package com.ssonsal.football.game.service;

import com.ssonsal.football.game.dto.request.ApprovalTeamRequestDto;
import com.ssonsal.football.game.dto.response.GameResultResponseDto;
import com.ssonsal.football.game.entity.ApplicantStatus;
import com.ssonsal.football.game.entity.Game;
import com.ssonsal.football.game.entity.MatchApplication;
import com.ssonsal.football.game.entity.MatchStatus;
import com.ssonsal.football.game.exception.GameErrorCode;
import com.ssonsal.football.game.repository.GameRepository;
import com.ssonsal.football.game.util.GameResultSet;
import com.ssonsal.football.game.util.TeamResult;
import com.ssonsal.football.global.exception.CustomException;
import com.ssonsal.football.team.entity.TeamRecord;
import com.ssonsal.football.team.repository.TeamRecordRepository;
import com.ssonsal.football.user.entity.User;
import com.ssonsal.football.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

import static com.ssonsal.football.game.exception.MatchErrorCode.NOT_APPLICANT_TEAM;
import static com.ssonsal.football.game.exception.MatchErrorCode.NOT_TEAM_MEMBER;
import static com.ssonsal.football.game.util.GameConstant.*;
import static com.ssonsal.football.game.util.Transfer.longIdToMap;
import static com.ssonsal.football.global.util.ErrorCode.NOT_EXIST;
import static com.ssonsal.football.global.util.ErrorCode.USER_NOT_FOUND;


@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class MatchTeamServiceImpl implements MatchTeamService {

    private final GameRepository gameRepository;
    private final UserRepository userRepository;
    private final TeamRecordRepository teamRecordRepository;


    @Transactional
    public Long approveAwayTeam(Long userId, Long gameId,
                                ApprovalTeamRequestDto approvalAwayTeamDto) {

        // 해당 게임
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new CustomException(NOT_EXIST));
        checkGameIsWaiting(game.getMatchStatus());

        // 요청을 보낸 user 가 게임을 생성한 팀의 팀원인지 확인
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND, longIdToMap(USER_ID, userId)));
        checkUserInHomeTeam(game, user);

        // Applicant Status 대기 -> 보류로 변경
        List<MatchApplication> applicantsTeams = game.getMatchApplications();
        applicantsTeams.stream()
                .filter(team -> team.getApplicationStatus().equals(ApplicantStatus.WAITING.getDescription()))
                .forEach(MatchApplication::changeStatusToWaiting);

        // 승인할 팀 신청
        Long teamId = approvalAwayTeamDto.getTeamId();
        checkIsAwayTeam(game, teamId);
        MatchApplication applicantTeam = applicantsTeams.stream()
                .filter(target -> Objects.equals(target.getTeam().getId(), teamId))
                .findAny()
                .orElseThrow(() -> new CustomException(NOT_APPLICANT_TEAM, longIdToMap(TEAM_ID, teamId)));

        // 승인 로직
        applicantTeam.approve();

        return game.getId();
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
        log.info("어웨이 팀 입력 시 homeResultInKo={}", homeResultInKo);
        if (homeResultInKo == null) {
            gameResultResponseDto.setTotalScore(awayScore);
            return gameResultResponseDto;
        }

        TeamResult homeResult = TeamResult.peekResult(homeResultInKo);
        Integer totalScore = homeResult.getScore() + awayScore;

        // 양 팀 모두 옳은 승패를 입력한 경우 team_record 테이블에 결과 기입 후,
        if (totalScore.equals(TeamResult.END.getScore())) {

            Long homeId = game.getHome().getId();
            Long awayId = game.getAway().getId();

            TeamRecord homeRecord = teamRecordRepository.findById(homeId)
                    .orElseThrow(() -> new CustomException(NOT_EXIST, longIdToMap(TEAM_RECORD_ID, homeId)));
            GameResultSet.getHomeRecordEntity(homeRecord, homeResult, awayResult);

            TeamRecord awayRecord = teamRecordRepository.findById(awayId)
                    .orElseThrow(() -> new CustomException(NOT_EXIST, longIdToMap(TEAM_RECORD_ID, awayId)));
            GameResultSet.getAwayRecordEntity(awayRecord, homeResult, awayResult);

            game.end();
            gameResultResponseDto.setTotalScore(totalScore);
            return gameResultResponseDto;
        }

        // 옳은 입력이나 한팀만 입력 외에는 양팀에서 입력한 결과를 초기화 시킨다.
        game.enterHomeTeamResult(null);
        game.enterAwayTeamResult(null);
        return GameResultResponseDto.builder().build();
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
        log.info("홈 팀 입력 시 awayResultInKo={}", awayResultInKo);
        if (awayResultInKo == null) {
            gameResultResponseDto.setTotalScore(homeScore);
            return gameResultResponseDto;
        }

        TeamResult awayResult = TeamResult.peekResult(awayResultInKo);
        Integer totalScore = homeScore + awayResult.getScore();

        // 양 팀 모두 옳은 승패를 입력한 경우 team_record 테이블에 결과 기입
        if (totalScore.equals(TeamResult.END.getScore())) {

            Long homeId = game.getHome().getId();
            Long awayId = game.getAway().getId();

            TeamRecord homeRecord = teamRecordRepository.findById(homeId)
                    .orElseThrow(() -> new CustomException(NOT_EXIST, longIdToMap(TEAM_RECORD_ID, homeId)));
            GameResultSet.getHomeRecordEntity(homeRecord, homeResult, awayResult);

            TeamRecord awayRecord = teamRecordRepository.findById(awayId)
                    .orElseThrow(() -> new CustomException(NOT_EXIST, longIdToMap(TEAM_RECORD_ID, awayId)));
            GameResultSet.getAwayRecordEntity(awayRecord, homeResult, awayResult);

            game.end();
            gameResultResponseDto.setTotalScore(totalScore);
            return gameResultResponseDto;
        }

        // 옳은 입력이나 한팀만 입력 외에는 양팀에서 입력한 결과를 초기화 시킨다.
        game.enterHomeTeamResult(null);
        game.enterAwayTeamResult(null);
        return GameResultResponseDto.builder().build();
    }

    private void checkIsAwayTeam(Game game, Long teamId) {
        if (game.getHome().getId().equals(teamId)) {
            throw new CustomException(NOT_APPLICANT_TEAM);
        }
    }

    private void checkUserInHomeTeam(Game game, User user) {
        if (!user.getTeam().equals(game.getHome())) {
            throw new CustomException(NOT_TEAM_MEMBER, longIdToMap(USER_ID, user.getId()));
        }
    }

    private void checkGameIsWaiting(int gameMatchStatus) {
        int waitingStatus = MatchStatus.WAITING.getCodeNumber();

        if (!(gameMatchStatus == waitingStatus)) {
            throw new CustomException(GameErrorCode.ALREADY_CONFIRMED_GAME);
        }
    }
}
