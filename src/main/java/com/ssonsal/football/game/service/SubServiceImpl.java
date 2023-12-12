package com.ssonsal.football.game.service;

import com.ssonsal.football.game.dto.request.ApprovalSubRequestDto;
import com.ssonsal.football.game.dto.response.SubsResponseDto;
import com.ssonsal.football.game.entity.Game;
import com.ssonsal.football.game.entity.MatchApplication;
import com.ssonsal.football.game.entity.Sub;
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

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

import static com.ssonsal.football.game.exception.GameErrorCode.NOT_EXIST_APPLICATION;
import static com.ssonsal.football.game.exception.GameErrorCode.NOT_IN_TARGET_TEAM;
import static com.ssonsal.football.game.exception.SubErrorCode.NOT_EXIST_SUB_APPLICANT;
import static com.ssonsal.football.game.util.GameConstant.*;
import static com.ssonsal.football.game.util.Transfer.longIdToMap;

@Slf4j
@RequiredArgsConstructor
@Service
public class SubServiceImpl implements SubService {

    private final SubApplicantRepository subApplicantRepository;
    private final SubRepository subRepository;
    private final MatchApplicationRepository matchApplicationRepository;
    private final UserRepository userRepository;


    @Override // 해당 팀 용병 목록
    public List<SubsResponseDto> getTeamSubList(Long matchApplicationId) {

        MatchApplication matchApplication = getMatchApplication(matchApplicationId);
        Team team = matchApplication.getTeam();
        Game game = matchApplication.getGame();

        // 해당 게임에 참여하는 각 팀에 소속된 용병 목록
        List<Sub> subs = subRepository.findByGameIdAndTeamId(team.getId(), game.getId());

        List<SubsResponseDto> subsResponseDtos = subs.stream()
                .map(SubsResponseDto::new)
                .collect(Collectors.toList());

        return subsResponseDtos;
    }


    @Override
    @Transactional // 용병 승인
    public Long acceptSub(Long loginUserId, Long matchApplicationId, ApprovalSubRequestDto approvalSubDto) {

        User loginUser = getUser(loginUserId);
        MatchApplication matchApplication = getMatchApplication(matchApplicationId);
        Long subApplicantId = approvalSubDto.getSubApplicantId();

        Team targetTeam = matchApplication.getTeam();
        validateUserInTargetTeam(targetTeam, loginUser.getTeam());

        SubApplicant subApplicant = subApplicantRepository.findById(subApplicantId)
                .orElseThrow(() -> new CustomException(NOT_EXIST_SUB_APPLICANT,
                        longIdToMap(SUB_APPLICANT_ID, subApplicantId)));

        subApplicant.accept();
        matchApplication.acceptSub();

        Sub sub = subRepository.save(Sub.builder()
                .user(subApplicant.getUser())
                .game(matchApplication.getGame())
                .team(loginUser.getTeam())
                .build());

        return sub.getId();

    }

    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND, longIdToMap(USER_ID, userId)));
    }

    private MatchApplication getMatchApplication(Long matchApplicationId) {

        return matchApplicationRepository.findById(matchApplicationId)
                .orElseThrow(() -> new CustomException(NOT_EXIST_APPLICATION,
                        longIdToMap(MATCH_APPLICATION_ID, matchApplicationId)));
    }

    private void validateUserInTargetTeam(Team targetTeam, Team userTeam) {

        if (!targetTeam.equals(userTeam)) {
            log.error("user 가 접근하려는 Team 의 팀원이 아님.");
            throw new CustomException(NOT_IN_TARGET_TEAM, longIdToMap(TEAM_ID, targetTeam.getId()));
        }
    }
}
