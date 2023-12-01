package com.ssonsal.football.user.repository;

import com.ssonsal.football.team.dto.response.TeamMemberListDto;
import com.ssonsal.football.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * 유저가 팀을 가지고 있는지 확인한다.
     *
     * @param userId 유저 아이디
     */
    boolean existsByIdAndTeamIsNull(Long userId);

    /**
     * 팀 회원 정보를 가져온다.
     *
     * @param teamId
     * @return 팀 회원 정보 목록
     */
    @Query("SELECT new com.ssonsal.football.team.dto.response.TeamMemberListDto(u.id, u.nickname, u.gender, u.position) FROM User u WHERE u.team.id = ?1")
    List<TeamMemberListDto> findAllByTeamId(Long teamId);

    /**
     * 팀의 평균 나이값을 구한다.
     * 
     * @param teamId 팀 아이디
     * @return 팀 평균 나이값
     */
    @Query("SELECT ROUND(AVG(YEAR(CURRENT_DATE) - YEAR(u.birth)), 1) FROM com.ssonsal.football.user.entity.User u WHERE u.team.id = :teamId")
    Float findAverageAgeByTeamId(@Param("teamId") Long teamId);

    /**
     * 유저가 특정 팀의 소속인지 확인한다.
     *
     * @param userId 유저 아이디
     * @param teamId 팀 아이디
     */
    boolean existsByIdAndTeamId(Long userId,Long teamId);

    /**
     * 유저의 나이를 구한다.
     *
     * @param userId 유저 아이디
     * @return 유저의 현재 나이
     */
    @Query("SELECT YEAR(CURRENT_DATE) - YEAR(u.birth) FROM User u WHERE u.id = :userId")
    Integer calculateAgeByUserId(@Param("userId") Long userId);

    /**
     * 팀의 회원 수를 구한다.
     *
     * @param teamId
     * @return 팀 회원 수 총합
     */
    Integer countByTeamId(Long teamId);
}
