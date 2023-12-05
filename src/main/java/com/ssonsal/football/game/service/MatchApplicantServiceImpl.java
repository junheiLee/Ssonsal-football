package com.ssonsal.football.game.service;

import com.ssonsal.football.game.dto.request.MatchApplicationRequestDto;
import com.ssonsal.football.game.entity.ApplicantStatus;
import com.ssonsal.football.game.entity.Game;
import com.ssonsal.football.game.entity.MatchApplication;
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
    public Long applyForGameAsAway(Long gameId, Long userId, MatchApplicationRequestDto applicationTeamDto) {

        // 공통 처리 고안 필수
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        Team team = user.getTeam();
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_EXIST));

        checkWriterInTeam(team);
        checkDuplicateApplication(team, game);

        MatchApplication matchApplication = matchTeamRepository.save(
                MatchApplication.builder()
                        .team(team)
                        .game(game)
                        .applicationStatus(ApplicantStatus.WAITING.getDescription())
                        .matchTeamDto(applicationTeamDto)
                        .build()
        );

        return matchApplication.getId();
    }

    private void checkDuplicateApplication(Team team, Game game) {

        if (Objects.equals(team, game.getHometeam())) {
            log.info("해당 게임에 이미 확정된 팀입니다.");
            throw new CustomException(MatchErrorCode.ALREADY_APPROVAL_TEAM);
        }

        List<MatchApplication> matchApplications = game.getMatchApplications();
        long count = matchApplications.stream().filter(matchTeam -> matchTeam.getTeam().equals(team)).count();

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
