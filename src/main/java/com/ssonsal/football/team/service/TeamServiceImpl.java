package com.ssonsal.football.team.service;

import com.ssonsal.football.global.exception.CustomException;
import com.ssonsal.football.global.util.AmazonS3Util;
import com.ssonsal.football.team.dto.response.*;
import com.ssonsal.football.team.entity.*;
import com.ssonsal.football.team.exception.TeamErrorCode;
import com.ssonsal.football.team.repository.TeamApplyRepository;
import com.ssonsal.football.team.repository.TeamRecordRepository;
import com.ssonsal.football.team.repository.TeamRejectRepository;
import com.ssonsal.football.team.repository.TeamRepository;
import com.ssonsal.football.user.entity.User;
import com.ssonsal.football.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.OptionalDouble;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class TeamServiceImpl implements TeamService {

    private final TeamRepository teamRepository;
    private final UserRepository userRepository;
    private final TeamRecordRepository teamRecordRepository;
    private final TeamApplyRepository teamApplyRepository;
    private final TeamRejectRepository teamRejectRepository;

    private final AmazonS3Util amazonS3Util;

    private final String defaultImg = "https://clclt-s3-1.s3.ap-northeast-2.amazonaws.com/normalTeam.png";


    /**
     * 모든 팀을 내림차순으로 정렬해서 가져온다.
     *
     * @return teamListDto 전체 팀 목록
     */
    @Override
    public List<TeamListDto> findAllTeams() {

        List<Team> teamList = teamRepository.findAllByOrderByIdDesc();
        List<TeamListDto> teams = teamList.stream()
                .map(team -> new TeamListDto(team, findRank(team.getId()), findAgeAverage(team.getId())))
                .collect(Collectors.toList());

        return teams;
    }

    /**
     * 모집 중인 팀만 내림차순으로 정렬해서 가져온다.
     *
     * @return 모집 중인 팀 목록
     */
    @Override
    public List<TeamListDto> findRecruitList() {

        List<Team> teamList = teamRepository.findAllByRecruitOrderByIdDesc(1);

        List<TeamListDto> teams = teamList.stream()
                .map(team -> new TeamListDto(team, findRank(team.getId()), findAgeAverage(team.getId())))
                .collect(Collectors.toList());

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

        List<Team> teamList = teamRepository.findAllByNameContaining(keyword);

        List<TeamListDto> teams = teamList.stream()
                .map(team -> new TeamListDto(team, findRank(team.getId()), findAgeAverage(team.getId())))
                .collect(Collectors.toList());

        return teams;
    }

    /**
     * 팀 상세정보를 가져온다.
     *
     * @param teamId 팀 아이디
     * @return 팀 상세정보
     */
    @Override
    public TeamDetailDto findTeamDetail(Long teamId) {

        TeamRecord teamRecord = teamRecordRepository.findById(teamId).orElseThrow(
                () -> new CustomException(TeamErrorCode.TEAM_NOT_FOUND));

        Team team = teamRepository.findById(teamId).orElseThrow(
                () -> new CustomException(TeamErrorCode.TEAM_NOT_FOUND));

        TeamDetailDto teamDetailDto = new TeamDetailDto(team, teamRecord, team.getUsers().size());

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

        List<User> users = teamRepository.findById(teamId)
                .map(Team::getUsers)
                .orElseThrow(() -> new CustomException(TeamErrorCode.TEAM_NOT_FOUND));

        List<TeamMemberListDto> memberList = users.stream()
                .map(member -> new TeamMemberListDto(member, calculateAge(member.getBirth())))
                .collect(Collectors.toList());

        return memberList;
    }

    /**
     * 팀장명을 가져온다.
     *
     * @param teamId 팀 아이디
     * @return 팀장 닉네임
     */
    @Override
    public String findLeaderName(Long teamId) {

        Team leaderId = teamRepository.findById(teamId).orElseThrow(
                () -> new CustomException(TeamErrorCode.TEAM_NOT_FOUND));

        User user = userRepository.findById(leaderId.getLeaderId()).orElseThrow(
                () -> new CustomException(TeamErrorCode.USER_NOT_FOUND));

        return user.getNickname();
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

        manage.put("teamLeader", findLeaderName(teamId));
        manage.put("teamId", teamId);
        manage.put("members", findMemberList(teamId));
        manage.put("applys", findApplyList(teamId));
        manage.put("rejects", findRejectList(teamId));

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

        List<TeamApply> applys = teamApplyRepository.findAllByTeamId(teamId);

        List<TeamApplyDto> applyList = applys.stream()
                .map(apply -> new TeamApplyDto(apply, calculateAge(apply.getUser().getBirth())))
                .collect(Collectors.toList());

        return applyList;
    }

    /**
     * 팀의 거절 또는 밴 유저 목록을 가져온다.
     *
     * @param teamId
     * @return
     */
    public List<TeamRejectDto> findRejectList(Long teamId) {

        List<TeamReject> rejects = teamRejectRepository.findAllByRejectId_TeamId(teamId);

        List<TeamRejectDto> rejectList = rejects.stream()
                .map(reject -> new TeamRejectDto(reject, calculateAge(reject.getRejectId().getUser().getBirth())))
                .collect(Collectors.toList());

        return rejectList;
    }

    /**
     * 팀 평균 나이대를 구한다.
     *
     * @param teamId 팀 아이디
     * @return 팀 평균 나이대
     */
    @Override
    public String findAgeAverage(Long teamId) {

        List<User> users = teamRepository.findById(teamId)
                .map(Team::getUsers)
                .orElseThrow(() -> new CustomException(TeamErrorCode.TEAM_NOT_FOUND));

        OptionalDouble averageValue = users.stream()
                .map(user -> calculateAge(user.getBirth()))
                .mapToDouble(Integer::doubleValue)
                .average();

        double average = averageValue.orElseThrow(
                () -> new CustomException(TeamErrorCode.TEAM_NOT_FOUND));

        String numberAsString = Double.toString(average);
        int frontOfAge = (int) Math.floor(average / 10) * 10;

        return frontOfAge + findAgeGroup(numberAsString.charAt(1));
    }

    /**
     * 팀 평균 나이대를 구한다.
     *
     * @param secondNumber 나이대 뒷자리
     * @return 초반/중반/후반 문자열
     */
    @Override
    public String findAgeGroup(char secondNumber) {
        int backOfAge = Character.getNumericValue(secondNumber);

        if (backOfAge >= 0 && backOfAge <= 3) {
            return AgeFormatter.EARLY.getAgeGroup();
        } else if (backOfAge >= 4 && backOfAge <= 6) {
            return AgeFormatter.MID.getAgeGroup();
        }

        return AgeFormatter.LATE.getAgeGroup();
    }

    /**
     * 유저의 현재 나이를 구한다.
     *
     * @param birth 유저의 생일
     * @return 현재 나이
     */
    private int calculateAge(LocalDate birth) {

        LocalDate currentDate = LocalDate.now();
        LocalDateTime birthDateTime = birth.atStartOfDay();

        long age = ChronoUnit.YEARS.between(birthDateTime, LocalDateTime.now());

        if (birthDateTime.plusYears(age).isAfter(LocalDateTime.now())) {
            age--;
        }

        return (int) age;
    }

    /**
     * 팀의 랭킹을 가져온다.
     *
     * @param teamId 팀 아이디
     * @return 팀 랭킹
     */
    public Integer findRank(Long teamId) {

        TeamRecord teamRecord = teamRecordRepository.findById(teamId).orElseThrow(
                () -> new CustomException(TeamErrorCode.TEAM_NOT_FOUND));

        return teamRecord.getRank();
    }


}
