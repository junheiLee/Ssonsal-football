package com.ssonsal.football.game.service;

import com.ssonsal.football.game.dto.response.SubApplicantsResponseDto;
import com.ssonsal.football.game.entity.ApplicantStatus;
import com.ssonsal.football.game.entity.MatchApplication;
import com.ssonsal.football.game.entity.SubApplicant;
import com.ssonsal.football.game.repository.MatchApplicationRepository;
import com.ssonsal.football.game.repository.SubApplicantRepository;
import com.ssonsal.football.game.repository.SubRepository;
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
import java.util.Map;
import java.util.stream.Collectors;

import static com.ssonsal.football.game.exception.GameErrorCode.*;
import static com.ssonsal.football.game.exception.SubErrorCode.*;
import static com.ssonsal.football.game.util.GameConstant.*;
import static com.ssonsal.football.game.util.Transfer.longIdToMap;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class SubApplicantServiceImpl implements SubApplicantService {

    private final SubApplicantRepository subApplicantRepository;
    private final SubRepository subRepository;
    private final MatchApplicationRepository matchApplicationRepository;
    private final UserRepository userRepository;

    @Override
    public List<SubApplicantsResponseDto> getSubApplicantsByGameAndTeam(Long teamId, Long gameId) {

        MatchApplication matchApplication = getMatchApplication(teamId, gameId);

        List<SubApplicant> SubApplicants = matchApplication.getSubApplicants();
        return SubApplicants.stream().map(SubApplicantsResponseDto::new).collect(Collectors.toList());
    }


    @Override
    @Transactional
    public Long applySubApplicant(Long userId, Long teamId, Long gameId) {

        User user = getUser(userId);
        MatchApplication matchApplication = getMatchApplication(teamId, gameId);

        validateMatchRequireSub(matchApplication.getSubCount(), matchApplicantInfoToMap(teamId, gameId));
        validateNotInTargetTeam(user, teamId);
        validateNotAlreadyApplication(userId, matchApplication);

        SubApplicant applicant = subApplicantRepository.save(SubApplicant.builder()
                .matchApplication(matchApplication)
                .user(user)
                .subApplicantStatus(ApplicantStatus.WAITING.getDescription())
                .build());

        return applicant.getId();
    }

    private void validateMatchRequireSub(int subCount, Map<String, Long> teamIdAndGameId) {

        if (subCount <= ZERO) {
            throw new CustomException(NOT_REQUIRED_SUB, teamIdAndGameId);
        }
    }

    private void validateNotInTargetTeam(User user, Long teamId) {
        if (user.getTeam().getId() == teamId) {
            throw new CustomException(ALREADY_IN_TEAM, longIdToMap(USER_ID, user.getId()));
        }
    }

    private void validateNotAlreadyApplication(Long userId, MatchApplication matchApplication) {
        if (subApplicantRepository.findByMatchApplication(matchApplication).size() > ZERO) {
            throw new CustomException(ALREADY_APPLICANT_SUB, longIdToMap(USER_ID, userId));
        }
    }

    @Override
    @Transactional
    public Long rejectSubApplicant(Long loginUserId, Long userTeamId, Long targetId) {

        User loginUser = getUser(loginUserId);
        Team loginUserTeam = getUserTeam(loginUser);

        SubApplicant subApplicant = subApplicantRepository.findById(targetId)
                .orElseThrow(() -> new CustomException(NOT_EXIST_SUB_APPLICANT, longIdToMap(SUB_APPLICANT_ID, targetId)));

        Team targetTeam = subApplicant.getMatchApplication().getTeam();
        User targetSub = subApplicant.getUser();
        validateInTargetTeam(targetTeam, loginUserTeam);

        subApplicant.reject();
        return targetSub.getId();
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

    private void validateInTargetTeam(Team targetTeam, Team userTeam) {

        if (!targetTeam.equals(userTeam)) {
            log.error("user 가 접근하려는 Team 의 팀원이 아님.");
            throw new CustomException(NOT_IN_TARGET_TEAM, longIdToMap(TEAM_ID, targetTeam.getId()));
        }
    }

    private MatchApplication getMatchApplication(Long teamId, Long gameId) {

        return matchApplicationRepository.findByTeamIdAndGameId(teamId, gameId)
                .orElseThrow(() -> new CustomException(NOT_EXIST_APPLICATION, matchApplicantInfoToMap(teamId, gameId)));
    }

    private Map<String, Long> matchApplicantInfoToMap(Long teamId, Long gameId) {
        Map<String, Long> teamIdAndGameId = longIdToMap(TEAM_ID, teamId);
        teamIdAndGameId.put(GAME_ID, gameId);

        return teamIdAndGameId;
    }

}
