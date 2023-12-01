package com.ssonsal.football.game.service;

import com.ssonsal.football.game.dto.request.GameRequestDto;
import com.ssonsal.football.game.dto.request.MatchTeamRequestDto;
import com.ssonsal.football.game.entity.ApplicantStatus;
import com.ssonsal.football.game.entity.Game;
import com.ssonsal.football.game.entity.MatchStatus;
import com.ssonsal.football.game.entity.MatchTeam;
import com.ssonsal.football.game.exception.GameErrorCode;
import com.ssonsal.football.game.repository.GameRepository;
import com.ssonsal.football.game.repository.MatchTeamRepository;
import com.ssonsal.football.global.exception.CustomException;
import com.ssonsal.football.team.entity.Team;
import com.ssonsal.football.team.repository.TeamRepository;
import com.ssonsal.football.user.entity.User;
import com.ssonsal.football.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class GameServiceImpl implements GameService {

    private final GameRepository gameRepository;
    private final MatchTeamRepository matchTeamRepository;
    private final UserRepository userRepository;
    private final TeamRepository teamRepository;


    @Transactional
    public Long createGame(Long userId, GameRequestDto gameRequestDto, MatchTeamRequestDto homeTeamRequestDto) throws ParseException {

        checkTargetIsExist(gameRequestDto.isFindAway(), homeTeamRequestDto.getSubCount());
        User user = userRepository.findById(userId).get();
        checkWriterIsInTeam(user);
        Team team = teamRepository.findById(gameRequestDto.getHometeamId()).get();

        Game game = gameRepository.save(
                Game.builder()
                        .writer(user)
                        .hometeam(team)
                        .matchStatus(isRequireAway(gameRequestDto.isFindAway()))
                        .schedule(stringToLocalDateTime(gameRequestDto.getSchedule()))
                        .gameRequestDto(gameRequestDto)
                        .build());
        matchTeamRepository.save(
                MatchTeam.builder()
                        .team(team)
                        .game(game)
                        .matchApplicantStatus(ApplicantStatus.APPROVAL.getDescription())
                        .matchTeamRequestDto(homeTeamRequestDto)
                        .build());

        return game.getId();
    }

    private void checkWriterIsInTeam(User user) {
        if (user.getTeam() == null) {
            log.error("팀만 게임 글 작성이 가능함.");
            throw new CustomException(GameErrorCode.WRITER_NOT_IN_TEAM);
        }
    }

    private void checkTargetIsExist(boolean findAway, int subCount) {
        if (!findAway && subCount == 0) {
            log.error("구하는 대상이 있어야 게임 글을 올릴 수 있음.");
            throw new CustomException(GameErrorCode.NOT_FOUND_TARGET);
        }
    }


    private LocalDateTime stringToLocalDateTime(String dateTime) throws ParseException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        log.info("stringToLocalDateTime = {}", dateTime);
        return LocalDateTime.parse(dateTime, formatter);
    }

    private MatchStatus isRequireAway(boolean isNeedAway) {
        if (isNeedAway) {
            return MatchStatus.WAITING;
        }
        return MatchStatus.MATCHING;
    }
}
