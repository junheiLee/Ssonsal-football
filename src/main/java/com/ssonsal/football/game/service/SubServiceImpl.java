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
import com.ssonsal.football.user.entity.User;
import com.ssonsal.football.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

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
        // 필요 용병 수 확인해서 필요없으면 신청 불가(matchteam ->subAcount)

        MatchApplication matchApplication = matchApplicationRepository.findByGameIdAndTeamId(gameId, teamId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_EXIST));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_EXIST));

        if(0 >= matchApplication.getSubCount()){
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

}
