package com.ssonsal.football.game.service;

import com.ssonsal.football.game.dto.request.GameRequestDto;
import com.ssonsal.football.game.dto.request.GameResultRequestDto;
import com.ssonsal.football.game.dto.request.MatchApplicationRequestDto;
import com.ssonsal.football.game.dto.response.GameDetailResponseDto;
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
import static com.ssonsal.football.game.util.Transfer.longIdToMap;
import static com.ssonsal.football.global.util.ErrorCode.FORBIDDEN_USER;

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
    public GameDetailResponseDto findGame(Long gameId) {

        Game game = getGame(gameId);
        MatchApplication homeApplication = matchApplicationRepository.findByTeamIdAndGameId(game.getHome().getId(), gameId)
                .orElse(null);

        Long awayId, awayApplicationId;
        if (game.getAway() == null) {
            awayId = null;
            awayApplicationId = null;
        } else {
            MatchApplication awayApplication =
                    matchApplicationRepository.findByTeamIdAndGameId(game.getAway().getId(), gameId).get();
            awayId = awayApplication.getTeam().getId();
            awayApplicationId = awayApplication.getId();
        }
        return new GameDetailResponseDto(game, homeApplication.getId(), awayId, awayApplicationId);
    }

    @Override
    @Transactional
    public Long createGame(Long loginUserId, GameRequestDto gameDto) {

        MatchApplicationRequestDto homeDto = new MatchApplicationRequestDto(gameDto);
        validateHasTarget(gameDto.isFindAway(), homeDto.getSubCount());
        User loginUser = getUser(loginUserId);
        Team home = getUserTeam(loginUser);

        Game game = gameRepository.save(
                Game.builder()
                        .writer(loginUser)
                        .home(home)
                        .matchStatus(isRequireAway(gameDto.isFindAway()))
                        .schedule(stringToLocalDateTime(gameDto.getDate() + " " + gameDto.getTime()))
                        .gameRequestDto(gameDto)
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
        log.info("stringToLocalDateTime = {}", dateTime);
        return LocalDateTime.parse(dateTime, formatter);
    }

    @Override
    @Transactional
    public GameResultResponseDto enterResult(Long loginUserId, Long gameId, GameResultRequestDto gameResultDto) {

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
    public List<GameListResponseDto> findAllGamesForTeam() {
        List<GameListResponseDto> asd = gameRepository.findAllByMatchStatus(MatchStatus.WAITING.getCodeNumber())
                .stream().map(GameListResponseDto::new).collect(Collectors.toList());

        log.info("for-team test={}", asd.size());
        return asd;
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

    @Override
    @Transactional
    @Deprecated
    public Long updateGame(Long loginUserId, Long gameId,
                           GameRequestDto updateGameDto, MatchApplicationRequestDto updateHomeTeamDto) {

        User loginUser = getUser(loginUserId);

        // 요청한 사람이 해당 게임 작성자인지 확인
        if (!gameRepository.existsByIdAndWriterEquals(gameId, loginUser)) {
            throw new CustomException(FORBIDDEN_USER);
        }

        // 게임 정보 변경
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_EXIST, longIdToMap(GAME_ID, gameId)));
        game.update(stringToLocalDateTime(updateGameDto.getDate() + " " + updateGameDto.getTime()), updateGameDto);

        MatchApplication homeTeam
                = matchApplicationRepository.findByTeamIdAndGameId(game.getHome().getId(), game.getId())
                .orElseThrow();
        homeTeam.update(updateHomeTeamDto);

        return gameId;
    }
}
