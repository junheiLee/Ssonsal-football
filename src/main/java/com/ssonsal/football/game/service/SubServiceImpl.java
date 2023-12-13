package com.ssonsal.football.game.service;

import com.ssonsal.football.game.entity.SubApplicant;
import com.ssonsal.football.game.repository.GameRepository;
import com.ssonsal.football.game.repository.SubApplicantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class SubServiceImpl implements SubService {
    @Autowired
    private SubApplicantRepository subApplicantRepository;

    @Autowired
    private GameRepository gameRepository;

    @Transactional
    public List<SubApplicant> list(Long gameId, Long teamId){
        List<SubApplicant> subApplicantList = subApplicantRepository.findByMatchTeamId(teamId);

        return subApplicantList;
    }

}
