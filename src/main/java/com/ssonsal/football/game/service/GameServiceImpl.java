package com.ssonsal.football.game.service;

import com.ssonsal.football.game.dto.request.GameRequestDto;
import com.ssonsal.football.game.dto.request.GameResultRequestDto;
import com.ssonsal.football.game.dto.request.MatchApplicationRequestDto;
import com.ssonsal.football.game.dto.response.GameListResponseDto;
import com.ssonsal.football.game.dto.response.GameResultResponseDto;
import com.ssonsal.football.game.entity.ApplicantStatus;
import com.ssonsal.football.game.entity.Game;
import com.ssonsal.football.game.entity.MatchApplication;
import com.ssonsal.football.game.entity.MatchStatus;
import com.ssonsal.football.game.exception.GameErrorCode;
import com.ssonsal.football.game.exception.MatchErrorCode;
import com.ssonsal.football.game.repository.GameRepository;
import com.ssonsal.football.game.repository.MatchApplicationRepository;
import com.ssonsal.football.game.util.TeamResult;
import com.ssonsal.football.global.exception.CustomException;
import com.ssonsal.football.global.util.ErrorCode;
import com.ssonsal.football.team.entity.Team;
import com.ssonsal.football.user.entity.User;
import com.ssonsal.football.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import static com.ssonsal.football.game.util.GameConstant.*;
import static com.ssonsal.football.game.util.Transfer.longIdToMap;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class GameServiceImpl implements GameService {

    private final MatchTeamService matchTeamService;
    private final GameRepository gameRepository;
    private final MatchApplicationRepository matchApplicationRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public Long createGame(Long userId, GameRequestDto gameDto, MatchApplicationRequestDto homeTeamDto) {

        checkTargetIsExist(gameDto.isFindAway(), homeTeamDto.getSubCount());
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND, longIdToMap(USER_ID, userId)));
        Team homeTeam = user.getTeam();
        checkWriterInTeam(homeTeam);

        Game game = gameRepository.save(
                Game.builder()
                        .writer(user)
                        .home(homeTeam)
                        .matchStatus(isRequireAway(gameDto.isFindAway()))
                        .schedule(stringToLocalDateTime(gameDto.getSchedule()))
                        .gameRequestDto(gameDto)
                        .build());

        matchApplicationRepository.save(
                MatchApplication.builder()
                        .applicant(user)
                        .team(homeTeam)
                        .game(game)
                        .applicationStatus(ApplicantStatus.APPROVAL.getDescription())
                        .matchTeamDto(homeTeamDto)
                        .build());

        return game.getId();
    }

    @Override
    @Transactional
    public GameResultResponseDto enterResult(Long userId, Long gameId, GameResultRequestDto gameResultDto) {

        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_EXIST, longIdToMap(GAME_ID, gameId)));
        checkAbleToEnterResult(game);

        String result = gameResultDto.getResult();

        // 해당 팀의 팀원이 한 요청인지 확인 후 matchTeamService 호출
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND, longIdToMap(USER_ID, userId)));
        if (gameResultDto.getTarget().equals(AWAY)) {

            checkUserInTeam(game.getAway(), user.getTeam());
            return matchTeamService.enterAwayTeamResult(game, TeamResult.peekResult(result));
        }
        if (gameResultDto.getTarget().equals(HOME)) {

            checkUserInTeam(game.getHome(), user.getTeam());
            return matchTeamService.enterHomeTeamResult(game, TeamResult.peekResult(result));
        }

        throw new CustomException(MatchErrorCode.IMPOSSIBLE_RESULT);
    }

    @Override
    public List<GameListResponseDto> findAllGamesForTeam() {

        return gameRepository.findAllByMatchStatus(MatchStatus.WAITING.getCodeNumber())
                .stream().map(GameListResponseDto::new).collect(Collectors.toList());
    }

    @Override
    public List<GameListResponseDto> findAllGamesForSub() {

        return gameRepository.searchAllGameForSub();
    }

    @Override
    public List<GameListResponseDto> findMyGamesAsSub(Long userId) {

        return gameRepository.searchMyGameAsSub(userId);
    }

    @Override
    public List<GameListResponseDto> findOurGamesAsTeam(Long teamId) {

        return gameRepository.searchOurGameAsTeam(teamId);
    }

    private void checkAbleToEnterResult(Game game) {
        if (game.getMatchStatus() != MatchStatus.CONFIRMED.getCodeNumber()) {
            log.error("대기 중이거나 종료된 게임은 결과를 기입할 수 없음.");
            throw new CustomException(GameErrorCode.CAN_NOT_ENTER_RESULT, game.getId());
        }
        if (game.getAway() == null) {
            log.error("상대 팀을 구하지 않는 게임 글인 경우 결과 기입 불가");
            throw new CustomException(GameErrorCode.CAN_NOT_ENTER_RESULT);
        }
    }

    private void checkUserInTeam(Team targetTeam, Team userTeam) {
        if (!targetTeam.equals(userTeam)) {
            log.error("user가 접근하려는 Team의 팀원이 아님.");
            throw new CustomException(MatchErrorCode.NOT_TEAM_MEMBER);
        }
    }

    private void checkWriterInTeam(Team team) {
        if (team == null) {
            throw new CustomException(GameErrorCode.WRITER_NOT_IN_TEAM);
        }
    }

    private void checkTargetIsExist(boolean findAway, int subCount) {
        if (!findAway && subCount == 0) {
            log.error("구하는 대상이 있어야 게임 글을 올릴 수 있음.");
            throw new CustomException(GameErrorCode.NOT_FOUND_TARGET);
        }
    }

    private LocalDateTime stringToLocalDateTime(String dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);
        log.info("stringToLocalDateTime = {}", dateTime);
        return LocalDateTime.parse(dateTime, formatter);
    }

    private MatchStatus isRequireAway(boolean isNeedAway) {
        if (isNeedAway) {
            return MatchStatus.WAITING;
        }
        return MatchStatus.CONFIRMED;
    }
}
