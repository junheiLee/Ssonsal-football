package com.ssonsal.football.game.service;

import com.ssonsal.football.game.dto.request.SubRecordDto;
import com.ssonsal.football.game.entity.Game;
import com.ssonsal.football.game.entity.Sub;
import com.ssonsal.football.game.entity.SubApplicant;
import com.ssonsal.football.game.repository.GameRepository;
import com.ssonsal.football.game.repository.SubApplicantRepository;
import com.ssonsal.football.game.repository.SubRepository;
import com.ssonsal.football.game.service.SubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class SubServiceImpl implements SubService {

    @Autowired
    private SubApplicantRepository subApplicantRepository;

    @Autowired
    private SubRepository subRepository;

    @Autowired
    private GameRepository gameRepository;

    @Transactional
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
}
