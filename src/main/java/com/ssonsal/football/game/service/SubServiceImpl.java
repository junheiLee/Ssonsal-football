package com.ssonsal.football.game.service;

import com.ssonsal.football.game.dto.request.SubRecordDto;
import com.ssonsal.football.game.entity.*;
import com.ssonsal.football.game.exception.SubErrorCode;
import com.ssonsal.football.game.repository.GameRepository;
import com.ssonsal.football.game.repository.MatchApplicationRepository;
import com.ssonsal.football.game.repository.SubApplicantRepository;
import com.ssonsal.football.game.repository.SubRepository;
import com.ssonsal.football.global.exception.CustomException;
import com.ssonsal.football.global.util.ErrorCode;
import com.ssonsal.football.team.entity.Team;
import com.ssonsal.football.team.repository.TeamRepository;
import com.ssonsal.football.user.entity.User;
import com.ssonsal.football.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class SubServiceImpl implements SubService {

    private final SubApplicantRepository subApplicantRepository;
    private final SubRepository subRepository;
    private final GameRepository gameRepository;
    private final TeamRepository teamRepository;
    private final MatchApplicationRepository matchApplicationRepository;
    private final UserRepository userRepository;

    @Transactional // 용병으로 참여한 기록
     public List<SubRecordDto> getSubRecordsByUserId(Long userId) {
        List<Sub> subRecords = subRepository.findByUser_Id(userId);
        List<SubRecordDto> subList = new ArrayList<>();

        for (Sub subRe : subRecords) {
            Game game = subRe.getGame();

            if (game != null) {
                LocalDateTime schedule = game.getSchedule();
                String gameRegion = game.getRegion();
                String stadium = game.getStadium();
                Integer vsFormat = game.getVsFormat();
                String gameRule = game.getRule();


                SubRecordDto SubBuilder = SubRecordDto.builder()
                        .schedule(game.getSchedule())
                        .gameRegion(game.getRegion())
                        .stadium(game.getStadium())
                        .vsFormat(game.getVsFormat())
                        .gameRule(game.getRule())
                        .build();

                subList.add(SubBuilder);
            }
        }
        return subList;
    }

    @Transactional // 용병 신청하기
    public String subApplicant(Long userId, Long gameId, Long teamId){
        String request="오류";
        MatchApplication matchApplication = matchApplicationRepository.findByGameIdAndTeamId(gameId, teamId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_EXIST));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_EXIST));


        // 필요 용병 수 확인해서 필요없으면 신청 불가(matchteam ->subAcount)
        //신청한 팀과 신청한 사람의 소속이 같지 않을때 신청가능
        if(0 >= matchApplication.getSubCount() && teamId == user.getTeam().getId()){
            throw new CustomException(SubErrorCode.CLOSED);
        }else{

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

    @Transactional // 용병 승인
    public String subAccept(Long userId, Long teamId, Long gameId){
        String request="오류";
        Long cookieId = 1L;

        MatchApplication matchApplication = matchApplicationRepository.findByGameIdAndTeamId(gameId, teamId)
                .orElseThrow(()->new CustomException(ErrorCode.NOT_PERMISSION));
        User loginUser = userRepository.findById(cookieId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_EXIST));

        if(loginUser.getTeam() == matchApplication.getTeam()){ // 현재 로그인 한 사람이 신청한 팀에 속해 있을때

            // 용병 신청아이디와 유저아이디가 일치하면
            SubApplicant subApplicants = subApplicantRepository.findByUserId(userId);
            subApplicants.UpdateSubStatus(ApplicantStatus.APPROVAL.getDescription());

            // 승인 후 용병 카운트 -1 (matchteam ->subAcount)
            matchApplication.decreaseSubCount();
            request="Success";

            // 승인된 용병을 Sub 테이블에 추가하기
            Sub savedSub = subRepository.save(Sub.builder()
                    .user(subApplicants.getUser())
                    .game(subApplicants.getMatchApplication().getGame())
                    .matchApplication(subApplicants.getMatchApplication())
                    .build());
        }
        return request;
    }

    @Transactional // 용병 거절
    public String subReject(Long userId, Long teamId, Long gameId){
        String request="오류";
        Long cookieId = 1L;

        MatchApplication matchApplication = matchApplicationRepository.findByGameIdAndTeamId(gameId, teamId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_EXIST));
        User loginUser = userRepository.findById(cookieId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_EXIST));

        if(loginUser.getTeam() != null && loginUser.getTeam().equals(matchApplication.getTeam())){ // 현재 로그인 한 사람이 신청한 팀에 속해 있을때

            // 용병 신청한 사람의 상태 값을 거절으로 변경
            SubApplicant subApplicants = subApplicantRepository.findByUserId(userId);
            subApplicants.UpdateSubStatus(ApplicantStatus.REFUSAL.getDescription());

            request="Success";

            // 거절된 용병을 Sub 테이블에 추가하기
            Sub savedSub = subRepository.save(Sub.builder()
                    .user(subApplicants.getUser())
                    .game(subApplicants.getMatchApplication().getGame())
                    .matchApplication(subApplicants.getMatchApplication())
                    .build());
        }
        return request;
    }

}
