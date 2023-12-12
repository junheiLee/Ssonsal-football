package com.ssonsal.football.game.service;

import com.ssonsal.football.game.dto.request.SubInTeamDto;
import com.ssonsal.football.game.entity.ApplicantStatus;
import com.ssonsal.football.game.entity.MatchApplication;
import com.ssonsal.football.game.entity.Sub;
import com.ssonsal.football.game.entity.SubApplicant;
import com.ssonsal.football.game.repository.MatchApplicationRepository;
import com.ssonsal.football.game.repository.SubApplicantRepository;
import com.ssonsal.football.game.repository.SubRepository;
import com.ssonsal.football.global.exception.CustomException;
import com.ssonsal.football.global.util.ErrorCode;
import com.ssonsal.football.user.entity.User;
import com.ssonsal.football.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class SubServiceImpl implements SubService {

    private final SubApplicantRepository subApplicantRepository;
    private final SubRepository subRepository;
    private final MatchApplicationRepository matchApplicationRepository;
    private final UserRepository userRepository;


    @Override // 해당 팀 용병 목록
    public List<SubInTeamDto> getTeamSubList(Long gameId, Long teamId) {
        MatchApplication matchApplication = matchApplicationRepository.findByGameIdAndTeamId(teamId, gameId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_EXIST));
        // 해당 게임에 참여하는 각 팀에 소속된 용병 목록
        List<Sub> teamSubList = subRepository.findByGameIdAndTeamId(teamId, gameId);
        // 용병 목록을 DTO로 매핑
        List<SubInTeamDto> subInTeamDtos = teamSubList.stream()
                .map(SubInTeamDto::mapToSubInTeamDto)
                .collect(Collectors.toList());

        return subInTeamDtos;
    }


    @Override
    @Transactional // 용병 승인
    public String subAccept(Long userId, Long teamId, Long gameId) {
        String request = "오류";
        Long cookieId = 1L;
        MatchApplication matchApplication = matchApplicationRepository.findByGameIdAndTeamId(teamId, gameId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_PERMISSION));
        User loginUser = userRepository.findById(cookieId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_EXIST));

        if (loginUser.getTeam().getId() == teamId) { // 현재 로그인 한 사람이 신청한 팀에 속해 있을때

            // 용병 신청한 사람의 상태 값을 수락으로 변경
            SubApplicant subApplicants = subApplicantRepository.findByUserId(userId);
            subApplicants.UpdateSubStatus(ApplicantStatus.APPROVAL.getDescription());

            // 승인 후 용병 카운트 -1 (matchteam ->subAcount)
            matchApplication.decreaseSubCount();
            request = "Success";

            // 승인된 용병을 Sub 테이블에 추가하기
            Sub savedSub = subRepository.save(Sub.builder()
                    .user(subApplicants.getUser())
                    .game(subApplicants.getMatchApplication().getGame())
                    .team(subApplicants.getMatchApplication().getTeam())
                    .build());
        }
        return request;
    }


}
