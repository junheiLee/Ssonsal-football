package com.ssonsal.football.game.service;

import com.ssonsal.football.game.dto.request.SubApplyListDto;
import com.ssonsal.football.game.entity.ApplicantStatus;
import com.ssonsal.football.game.entity.MatchApplication;
import com.ssonsal.football.game.entity.Sub;
import com.ssonsal.football.game.entity.SubApplicant;
import com.ssonsal.football.game.exception.SubErrorCode;
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

@Slf4j
@RequiredArgsConstructor
@Service
public class SubApplicantServiceImpl implements SubApplicantService {

    private final SubApplicantRepository subApplicantRepository;
    private final SubRepository subRepository;
    private final MatchApplicationRepository matchApplicationRepository;
    private final UserRepository userRepository;

    @Override// 팀에 신청한 용병 현황
    public List<SubApplyListDto> getSubRecordsByGameAndTeamId(Long userId, Long gameId, Long teamId) {
        MatchApplication matchApplication = matchApplicationRepository.findByGameIdAndTeamId(teamId, gameId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_EXIST));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_EXIST));

        if (teamId.equals(user.getTeam().getId())) {
            // 해당 팀에 신청한 모든 용병 신청 기록 가져오기
            List<SubApplicant> subApplicants = subApplicantRepository.findByMatchApplication(matchApplication.getTeam().getId());
            List<SubApplyListDto> mapSubDto = SubApplyListDto.mapSubApplicantsToDto(subApplicants);

            return mapSubDto;
        }

        return null;
    }


    @Override
    @Transactional // 용병 신청하기
    public String subApplicant(Long userId, Long gameId, Long teamId) {
        String request = "오류";
        MatchApplication matchApplication = matchApplicationRepository.findByGameIdAndTeamId(teamId, gameId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_EXIST));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_EXIST));
        if (!(subRepository.findById(userId).isEmpty())) {
            log.info("00000");
            Sub sub = subRepository.findById(userId)
                    .orElseThrow(() -> new CustomException(ErrorCode.NOT_EXIST));
        }

        // 필요 용병 수 확인해서 필요없으면 신청 불가(matchteam ->subAcount)
        //신청한 팀과 신청한 사람의 소속이 같지 않을때 신청가능
        if (matchApplication.getSubCount() <= 0 || teamId != user.getTeam().getId()) {
            throw new CustomException(SubErrorCode.CLOSED);
        } else {
            subApplicantRepository.save(SubApplicant.builder()
                    .matchApplication(matchApplication)
                    .user(user)
                    .subApplicantStatus(ApplicantStatus.WAITING.getDescription())
                    .build());

            log.info("신청 성공");
            request = "신청 성공";

        }
        return request;
    }


    @Override
    @Transactional // 용병 거절
    public String subReject(Long userId, Long teamId, Long gameId) {
        String request = "오류";
        Long cookieId = 1L;

        MatchApplication matchApplication = matchApplicationRepository.findByGameIdAndTeamId(teamId, gameId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_EXIST));
        User loginUser = userRepository.findById(cookieId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_EXIST));

        if (loginUser.getTeam().getId() == teamId) { // 현재 로그인 한 사람이 신청한 팀에 속해 있을때

            // 용병 신청한 사람의 상태 값을 거절으로 변경
            SubApplicant subApplicants = subApplicantRepository.findByUserId(userId);
            subApplicants.UpdateSubStatus(ApplicantStatus.REFUSAL.getDescription());

            request = "Success";

            // 거절된 용병을 Sub 테이블에 추가하기
            Sub savedSub = subRepository.save(Sub.builder()
                    .user(subApplicants.getUser())
                    .game(subApplicants.getMatchApplication().getGame())
                    .team(null)
                    .build());
        }
        return request;
    }
}
