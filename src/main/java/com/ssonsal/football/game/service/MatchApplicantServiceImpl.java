package com.ssonsal.football.game.service;

import com.ssonsal.football.game.dto.request.MatchApplicantRequestDto;
import com.ssonsal.football.game.entity.Game;
import com.ssonsal.football.game.entity.MatchStatus;
import com.ssonsal.football.game.entity.MatchTeam;
import com.ssonsal.football.game.exception.GameErrorCode;
import com.ssonsal.football.game.exception.MatchErrorCode;
import com.ssonsal.football.game.repository.GameRepository;
import com.ssonsal.football.game.repository.MatchTeamRepository;
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

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class MatchApplicantServiceImpl implements MatchApplicantService {

    private final UserRepository userRepository;
    private final GameRepository gameRepository;
    private final MatchTeamRepository matchTeamRepository;

    @Transactional
    public Long applyForGameAsAway(Long gameId, Long userId, MatchApplicantRequestDto awayteamDto){

        // 공통 처리 고안 필수
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        Team team = user.getTeam();
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_EXIST));

        checkWriterInTeam(team);
        checkDuplicateApplicant(team, game);

        MatchTeam matchTeam = matchTeamRepository.save(
                MatchTeam.builder()
                        .team(team)
                        .game(game)
                        .matchApplicantStatus(MatchStatus.WAITING.getDescription())
                        .matchTeamDto(awayteamDto)
                        .build()
        );

        return matchTeam.getId();
    }

    private void checkDuplicateApplicant(Team team, Game game) {

        if(Objects.equals(team, game.getHometeam())) {
            log.info("해당 게임에 이미 확정된 팀입니다.");
            throw new CustomException(MatchErrorCode.ALREADY_APPROVAL_TEAM);
        }

        List<MatchTeam> matchTeams = game.getMatchTeams();
        long count = matchTeams.stream().filter(matchTeam -> matchTeam.getTeam().equals(team)).count();

        if (count != 0) {
            log.info("해당 게임에 이미 신청한 팀입니다.");
            throw new CustomException(MatchErrorCode.ALREADY_APPLICANT_TEAM);
        }
    }

    private void checkWriterInTeam(Team team) {
        if (team == null) {
            throw new CustomException(GameErrorCode.WRITER_NOT_IN_TEAM);
        }
    }
}
