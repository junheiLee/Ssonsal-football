package com.ssonsal.football.game.service;

import com.ssonsal.football.game.dto.request.CreateGameRequestDto;
import com.ssonsal.football.game.dto.request.CreateMatchApplicationRequestDto;
import com.ssonsal.football.game.dto.request.EnterResultRequestDto;
import com.ssonsal.football.game.dto.response.GameInfoResponseDto;
import com.ssonsal.football.game.dto.response.GameListResponseDto;
import com.ssonsal.football.game.dto.response.GameResultResponseDto;
import com.ssonsal.football.game.entity.Game;
import com.ssonsal.football.game.entity.MatchApplication;
import com.ssonsal.football.game.entity.MatchStatus;
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

import static com.ssonsal.football.game.entity.ApplicantStatus.APPROVAL;
import static com.ssonsal.football.game.entity.MatchStatus.CONFIRMED;
import static com.ssonsal.football.game.exception.GameErrorCode.*;
import static com.ssonsal.football.game.util.GameConstant.*;
import static com.ssonsal.football.global.util.transfer.Transfer.longIdToMap;

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
    public Long insertGame(Long loginUserId, CreateGameRequestDto gameDto) {

        CreateMatchApplicationRequestDto homeDto = new CreateMatchApplicationRequestDto(gameDto);
        validateHasTarget(gameDto.isFindAway(), homeDto.getSubCount());
        User loginUser = getUser(loginUserId);
        Team home = getUserTeam(loginUser);

        Game game = gameRepository.save(
                Game.builder()
                        .writer(loginUser)
                        .home(home)
                        .matchStatus(isRequireAway(gameDto.isFindAway()))
                        .schedule(stringToLocalDateTime(gameDto.getDate() + " " + gameDto.getTime()))
                        .createGameRequestDto(gameDto)
                        .build());

        matchApplicationRepository.save(
                MatchApplication.builder()
                        .applicant(loginUser)
                        .team(home)
                        .game(game)
                        .applicationStatus(APPROVAL.getDescription())
                        .matchTeamDto(homeDto)
                        .build());

        return game.getId();
    }

    private void validateHasTarget(boolean findAway, int subCount) {
        if (!findAway && subCount == 0) {
            log.error("구하는 대상이 있어야 게임 글을 올릴 수 있음.");
            throw new CustomException(NOT_FOUND_TARGET);
        }
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND, longIdToMap(USER_ID, userId)));
    }

    private Team getUserTeam(User user) {
        Team userTeam = user.getTeam();

        if (userTeam == null) {
            throw new CustomException(NOT_IN_TEAM);
        }
        return userTeam;
    }

    private MatchStatus isRequireAway(boolean isNeedAway) {
        if (isNeedAway) {
            return MatchStatus.WAITING;
        }
        return CONFIRMED;
    }

    private LocalDateTime stringToLocalDateTime(String dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);
        return LocalDateTime.parse(dateTime, formatter);
    }

    @Override
    public GameInfoResponseDto findGameInfo(Long gameId) {

        Game game = getGame(gameId);
        MatchApplication homeApplication = getTeamApplication(game.getHome().getId(), game.getId());

        Long awayId, awayApplicationId;
        if (game.getAway() == null) {
            awayId = null;
            awayApplicationId = null;
        } else {
            MatchApplication awayApplication = getTeamApplication(game.getAway().getId(), game.getId());
            awayId = awayApplication.getTeam().getId();
            awayApplicationId = awayApplication.getId();
        }
        return new GameInfoResponseDto(game, homeApplication.getId(), awayId, awayApplicationId);
    }

    private MatchApplication getTeamApplication(Long teamId, Long gameId) {
        return matchApplicationRepository.findByTeamIdAndGameId(teamId, gameId)
                .orElse(null);
    }

    @Override
    @Transactional
    public GameResultResponseDto enterResult(Long loginUserId, Long gameId, EnterResultRequestDto gameResultDto) {

        Game game = getGame(gameId);
        validateAbleToEnterResult(game);

        User loginUser = getUser(loginUserId);

        String result = gameResultDto.getResult();
        String target = gameResultDto.getTarget();

        // 해당 팀의 팀원이 한 요청인지 확인 후 matchTeamService 호출
        if (target.equals(HOME)) {

            validateUserInTargetTeam(game.getHome(), loginUser.getTeam());
            return matchTeamService.enterHomeTeamResult(game, TeamResult.peekResult(result));
        }

        if (target.equals(AWAY)) {

            validateUserInTargetTeam(game.getAway(), loginUser.getTeam());
            return matchTeamService.enterAwayTeamResult(game, TeamResult.peekResult(result));
        }

        throw new CustomException(IMPOSSIBLE_RESULT);
    }

    private Game getGame(Long gameId) {
        return gameRepository.findByIdAndDeleteCodeIs(gameId, NOT_DELETED)
                .orElseThrow(() -> new CustomException(NOT_EXIST_GAME, longIdToMap(GAME_ID, gameId)));
    }

    private void validateAbleToEnterResult(Game game) {
        if (game.getMatchStatus() != CONFIRMED.getCodeNumber()) {
            log.error("대기 중이거나 종료된 게임은 결과를 기입할 수 없음.");
            throw new CustomException(CAN_NOT_ENTER_RESULT, longIdToMap(GAME_ID, game.getId()));
        }
        if (game.getAway() == null) {
            log.error("확정이지만 상대 팀을 구하지 않는 게임 글인 경우 결과 기입 불가");
            throw new CustomException(CAN_NOT_ENTER_RESULT, longIdToMap(GAME_ID, game.getId()));
        }
    }

    private void validateUserInTargetTeam(Team targetTeam, Team userTeam) {
        if (!targetTeam.equals(userTeam)) {
            log.error("user 가 접근하려는 Team 의 팀원이 아님.");
            throw new CustomException(NOT_IN_TARGET_TEAM, longIdToMap(TEAM_ID, targetTeam.getId()));
        }
    }

    @Override
    public List<GameListResponseDto> findAllGames() {

        return gameRepository.findAll().stream().map(GameListResponseDto::new).collect(Collectors.toList());
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
    public List<GameListResponseDto> findGamesBySub(Long userId) {

        return gameRepository.searchGameBySub(userId);
    }

    @Override
    public List<GameListResponseDto> findGamesByTeam(Long teamId) {

        return gameRepository.searchGameByTeam(teamId);
    }

}
