package com.ssonsal.football.game.service;

import com.ssonsal.football.game.dto.response.SubApplicantsResponseDto;
import com.ssonsal.football.game.entity.MatchApplication;
import com.ssonsal.football.game.entity.SubApplicant;
import com.ssonsal.football.game.repository.MatchApplicationRepository;
import com.ssonsal.football.game.repository.SubApplicantRepository;
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

import static com.ssonsal.football.game.entity.ApplicantStatus.WAITING;
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
    private final MatchApplicationRepository matchApplicationRepository;
    private final UserRepository userRepository;

    @Override
    public List<SubApplicantsResponseDto> getSubApplicantsByMatchApplication(Long matchApplicationId) {

        MatchApplication matchApplication = getMatchApplication(matchApplicationId);

        List<SubApplicant> SubApplicants
                = subApplicantRepository.findAllByMatchApplicationAndSubApplicantStatus(matchApplication,
                WAITING.getDescription());
        return SubApplicants.stream().map(SubApplicantsResponseDto::new).collect(Collectors.toList());
    }


    @Override
    @Transactional
    public Long applySubApplicant(Long userId, Long matchApplicationId) {

        User user = getUser(userId);
        MatchApplication matchApplication = getMatchApplication(matchApplicationId);

        validateRequireSub(matchApplication.getSubCount(), longIdToMap(MATCH_APPLICATION_ID, matchApplicationId));
        validateNotInTargetTeam(user.getTeam(), matchApplication.getTeam());
        validateNotAlreadyApplication(userId, matchApplication);

        SubApplicant applicant = subApplicantRepository.save(
                SubApplicant.builder()
                        .matchApplication(matchApplication)
                        .user(user)
                        .subApplicantStatus(WAITING.getDescription())
                        .build());

        return applicant.getId();
    }

    private void validateRequireSub(int subCount, Map<String, Long> matchApplicantMap) {

        if (subCount <= ZERO) {
            throw new CustomException(NOT_REQUIRED_SUB, matchApplicantMap);
        }
    }

    private void validateNotInTargetTeam(Team userTeam, Team targetTeam) {
        if (userTeam != null && userTeam == targetTeam) {
            throw new CustomException(ALREADY_IN_TEAM, longIdToMap(TEAM_ID, targetTeam.getId()));
        }
    }

    private void validateNotAlreadyApplication(Long userId, MatchApplication matchApplication) {
        if (subApplicantRepository.findByUserIdAndMatchApplication(userId, matchApplication).size() > ZERO) {
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

    private MatchApplication getMatchApplication(Long matchApplicationId) {

        return matchApplicationRepository.findById(matchApplicationId)
                .orElseThrow(() -> new CustomException(NOT_EXIST_APPLICATION,
                        longIdToMap(MATCH_APPLICATION_ID, matchApplicationId)));
    }

}
