package com.ssonsal.football.team.service;

import com.ssonsal.football.team.dto.response.TeamDetailDto;
import com.ssonsal.football.team.dto.response.TeamListDto;
import com.ssonsal.football.team.dto.response.TeamMemberListDto;
import com.ssonsal.football.team.entity.Team;
import com.ssonsal.football.team.repository.TeamRecordRepository;
import com.ssonsal.football.team.repository.TeamRepository;
import com.ssonsal.football.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class TeamServiceImpl implements TeamService {

    private final TeamRepository teamRepository;
    private final UserRepository userRepository;
    private final TeamRecordRepository teamRecordRepository;

    /**
     * 모든 팀을 내림차순으로 정렬해서 가져온다.
     *
     * @return teamListDto 전체 팀 목록
     */
    @Override
    public List<TeamListDto> findList() {

        List<TeamListDto> team = teamRepository.findAllByOrderByIdDesc();

        for (TeamListDto teamListDto : team) {
            teamListDto.setRanking(findRanking(teamListDto.getId()));
            teamListDto.setAgeAverage(userRepository.findAverageAgeByTeamId(teamListDto.getId()));
        }

        return team;
    }

    /**
     * 모집 중인 팀만 내림차순으로 정렬해서 가져온다.
     *
     * @return 모집 중인 팀 목록
     */
    @Override
    public List<TeamListDto> findRecruitList() {

        List<TeamListDto> team = teamRepository.findAllByRecruitOrderByIdDesc(1);

        for (TeamListDto teamListDto : team) {
            teamListDto.setRanking(findRanking(teamListDto.getId()));
            teamListDto.setAgeAverage(userRepository.findAverageAgeByTeamId(teamListDto.getId()));
        }

        return team;
    }

    /**
     * 검색한 팀명에 맞는 팀을 가져온다.
     *
     * @param keyword
     * @return 검색한 팀 목록
     */
    @Override
    public List<TeamListDto> searchName(String keyword) {

        List<TeamListDto> team = teamRepository.findByNameContaining(keyword);

        for (TeamListDto teamListDto : team) {
            teamListDto.setRanking(findRanking(teamListDto.getId()));
            teamListDto.setAgeAverage(userRepository.findAverageAgeByTeamId(teamListDto.getId()));
        }

        return team;
    }

    /**
     * 팀 상세정보를 가져온다.
     *
     * @param teamId 팀 아이디
     * @return 팀 상세정보
     */
    @Override
    public TeamDetailDto findDetail(Long teamId) {

        TeamDetailDto teamDetailDto = teamRepository.findTeamDtoWithRecord(teamId);

        teamDetailDto.setRanking(findRanking(teamId));
        teamDetailDto.setMemberCount(userRepository.countByTeamId(teamId));
        teamDetailDto.setLeaderName(findLeader(teamId));

        return teamDetailDto;
    }

    /**
     * 팀의 회원목록을 가져온다.
     *
     * @param teamId 팀 아이디
     * @return 팀 회원 정보 목록
     */
    @Override
    public List<TeamMemberListDto> findMemberList(Long teamId) {

        List<TeamMemberListDto> teamMembers = userRepository.findAllByTeamId(teamId);

        for (TeamMemberListDto teamMember : teamMembers) {
            teamMember.setBirth(userRepository.calculateAgeByUserId(teamMember.getId()));
        }

        return teamMembers;
    }

    /**
     * 팀장명을 가져온다.
     *
     * @param teamId 팀 아이디
     * @return 팀장 닉네임
     */
    @Override
    public String findLeader(Long teamId) {

        Team leaderId = teamRepository.findById(teamId).get();

        return userRepository.findById(leaderId.getLeaderId()).get().getNickname();
    }

    /**
     * 팀 랭킹을 구한다
     *
     * @param teamId 팀 아이디
     * @return 팀 랭킹값
     */
    @Override
    public Integer findRanking(Long teamId) {

        Integer ranking = teamRecordRepository.findRankById(teamId);

        return ranking;
    }

}
