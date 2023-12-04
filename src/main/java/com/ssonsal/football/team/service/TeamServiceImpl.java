package com.ssonsal.football.team.service;

import com.ssonsal.football.team.dto.response.TeamApplyDto;
import com.ssonsal.football.team.dto.response.TeamDetailDto;
import com.ssonsal.football.team.dto.response.TeamListDto;
import com.ssonsal.football.team.dto.response.TeamMemberListDto;
import com.ssonsal.football.team.entity.AgeFormatter;
import com.ssonsal.football.team.entity.Team;
import com.ssonsal.football.team.repository.TeamApplyRepository;
import com.ssonsal.football.team.repository.TeamRecordRepository;
import com.ssonsal.football.team.repository.TeamRepository;
import com.ssonsal.football.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class TeamServiceImpl implements TeamService {

    private final TeamRepository teamRepository;
    private final UserRepository userRepository;
    private final TeamRecordRepository teamRecordRepository;
    private final TeamApplyRepository teamApplyRepository;

    /**
     * 모든 팀을 내림차순으로 정렬해서 가져온다.
     *
     * @return teamListDto 전체 팀 목록
     */
    @Override
    public List<TeamListDto> findAllTeams() {

        List<TeamListDto> teams = teamRepository.findAllByOrderByIdDesc();

        for (TeamListDto teamListDto : teams) {
            teamListDto.setRank(findRank(teamListDto.getId()));
            teamListDto.setAgeAverage(findAgeAverage(teamListDto.getId()));
        }

        return teams;
    }

    /**
     * 모집 중인 팀만 내림차순으로 정렬해서 가져온다.
     *
     * @return 모집 중인 팀 목록
     */
    @Override
    public List<TeamListDto> findRecruitList() {

        List<TeamListDto> teams = teamRepository.findAllByRecruitOrderByIdDesc(1);

        for (TeamListDto teamListDto : teams) {
            teamListDto.setRank(findRank(teamListDto.getId()));
            teamListDto.setAgeAverage(findAgeAverage(teamListDto.getId()));
        }


        return teams;
    }

    /**
     * 검색한 팀명에 맞는 팀을 가져온다.
     *
     * @param keyword
     * @return 검색한 팀 목록
     */
    @Override
    public List<TeamListDto> findSearchList(String keyword) {

        List<TeamListDto> teams = teamRepository.findByNameContaining(keyword);

        for (TeamListDto teamListDto : teams) {
            teamListDto.setRank(findRank(teamListDto.getId()));
            teamListDto.setAgeAverage(findAgeAverage(teamListDto.getId()));
        }

        return teams;
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

        teamDetailDto.setRanking(findRank(teamId));
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

        List<TeamMemberListDto> teamMembers = teamRepository.findTeamMemberDtoById(teamId);

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
     * 팀원정보와 팀 신청목록을 가져온다.
     *
     * @param teamId 팀 아이디
     * @return map 관리 대상 목록이 담긴 map
     */
    @Override
    public Map<String, Object> findManageList(Long teamId) {

        Map<String, Object> manage = new HashMap<>();

        manage.put("teamLeader", findLeader(teamId));
        manage.put("teamId", teamId);
        manage.put("members", findMemberList(teamId));
        manage.put("applys", findApplyList(teamId));

        return manage;
    }

    /**
     * 팀에 신청한 유저 목록을 가져온다.
     *
     * @param teamId 팀 id
     * @return 신청한 유저 정보 목록
     */
    @Override
    public List<TeamApplyDto> findApplyList(Long teamId) {

        List<TeamApplyDto> applys = teamApplyRepository.findTeamAppliesWithUserAge(teamId);

        return applys;
    }

    /**
     * 팀 랭킹을 구한다
     *
     * @param teamId 팀 아이디
     * @return 팀 랭킹값
     */
    @Override
    public Integer findRank(Long teamId) {

        Integer ranking = teamRecordRepository.findRankById(teamId);

        return ranking;
    }

    /**
     * 팀 평균 나이대를 구한다.
     *
     * @param teamId 팀 아이디
     * @return 팀 평균 나이대
     */
    @Override
    public String findAgeAverage(Long teamId) {
        Integer average = teamRepository.getTeamAgeAverage(teamId);

        String numberAsString = Integer.toString(average);
        int firstNumber = (average / 10) * 10;

        return firstNumber + findAgeGroup(numberAsString.charAt(1));
    }

    /**
     * * 팀 평균 나이대를 구한다.
     *
     * @param secondNumber 나이대 뒷자리
     * @return 초반/중반/후반 문자열
     */
    @Override
    public String findAgeGroup(char secondNumber) {
        int second = Character.getNumericValue(secondNumber);
        if (second >= 0 && second <= 3) {
            return AgeFormatter.EARLY.getAgeGroup();
        } else if (second >= 4 && second <= 6) {
            return AgeFormatter.MID.getAgeGroup();
        } else {
            return AgeFormatter.LATE.getAgeGroup();
        }
    }

}
