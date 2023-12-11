package com.ssonsal.football.game.service;

import com.ssonsal.football.game.dto.request.MatchApplicationRequestDto;
import com.ssonsal.football.game.dto.response.MatchApplicationsResponseDto;
import com.ssonsal.football.game.entity.Game;
import com.ssonsal.football.game.entity.MatchApplication;
import com.ssonsal.football.game.entity.MatchStatus;
import com.ssonsal.football.game.exception.GameErrorCode;
import com.ssonsal.football.game.repository.GameRepository;
import com.ssonsal.football.game.repository.MatchApplicationRepository;
import com.ssonsal.football.game.util.Transfer;
import com.ssonsal.football.global.exception.CustomException;
import com.ssonsal.football.global.util.ErrorCode;
import com.ssonsal.football.team.entity.Team;
import com.ssonsal.football.user.entity.User;
import com.ssonsal.football.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

import static com.ssonsal.football.game.entity.ApplicantStatus.WAITING;
import static com.ssonsal.football.game.exception.GameErrorCode.*;
import static com.ssonsal.football.game.util.GameConstant.*;
import static com.ssonsal.football.game.util.Transfer.longIdToMap;
import static com.ssonsal.football.global.util.ErrorCode.FORBIDDEN_USER;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class MatchApplicantServiceImpl implements MatchApplicantService {

    private final UserRepository userRepository;
    private final GameRepository gameRepository;
    private final MatchApplicationRepository matchApplicationRepository;

    @Override
    public List<MatchApplicationsResponseDto> findAWaitingApplications(Long gameId) {
        return matchApplicationRepository.findByGameIdAndApplicationStatusIs(gameId, WAITING.getDescription());
    }

    @Transactional
    public Long applyToMatchAsAway(Long userId, Long gameId, MatchApplicationRequestDto applicationTeamDto) {

        User user = getUser(userId);
        Team team = validateUserInTeam(user.getTeam());
        Game game = getGame(gameId);

        validateGameIsWaiting(game);
        validateDuplicationApplication(team, game);

        MatchApplication matchApplication = matchApplicationRepository.save(
                MatchApplication.builder()
                        .applicant(user)
                        .team(team)
                        .game(game)
                        .applicationStatus(WAITING.getDescription())
                        .matchTeamDto(applicationTeamDto)
                        .build()
        );

        return matchApplication.getId();
    }

    private Team validateUserInTeam(Team userTeam) {

        if (userTeam == null) {
            throw new CustomException(GameErrorCode.NOT_IN_TEAM);
        }
        return userTeam;
    }

    private Game getGame(Long gameId) {
        return gameRepository.findByIdAndDeleteCodeIs(gameId, NOT_DELETED)
                .orElseThrow(() -> new CustomException(GameErrorCode.NOT_EXIST_GAME, longIdToMap(GAME_ID, gameId)));
    }

    private void validateGameIsWaiting(Game game) {

        if (game.getMatchStatus() != MatchStatus.WAITING.getCodeNumber()) {
            throw new CustomException(NOT_WAITING_GAME, longIdToMap(GAME_ID, game.getId()));
        }
    }

    private void validateDuplicationApplication(Team team, Game game) {

        if (Objects.equals(team, game.getHome())) {
            log.info("해당 게임의 등록 팀입니다.");
            throw new CustomException(ALREADY_APPROVAL_TEAM);
        }

        List<MatchApplication> matchApplications = game.getMatchApplications();
        long count = matchApplications.stream().filter(matchTeam -> matchTeam.getTeam().equals(team)).count();

        if (count != 0) {
            log.info("해당 게임에 이미 신청한 팀입니다.");
            throw new CustomException(ALREADY_APPLICANT_TEAM);
        }
    }

    @Transactional
    public Long rejectMatchApplication(Long userId, Long gameId, Long matchApplicationId) {
        User user = getUser(userId);
        validateUserPermission(user, gameId);

        MatchApplication matchApplication
                = matchApplicationRepository.findById(matchApplicationId)
                .orElseThrow(
                        () -> new CustomException(NOT_EXIST_APPLICATION,
                                longIdToMap(MATCH_APPLICATION_ID, matchApplicationId)));

        matchApplication.reject();
        return matchApplication.getId();
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND, longIdToMap(USER_ID, userId)));
    }

    private void validateUserPermission(User user, Long gameId) {

        if (!gameRepository.existsByIdAndWriterEquals(gameId, user)) {

            log.error("게임의 작성자만 신청을 거절할 수 있습니다. userId ={}", user.getId());
            throw new CustomException(FORBIDDEN_USER, Transfer.longIdToMap(USER_ID, user.getId()));
        }
    }

}
