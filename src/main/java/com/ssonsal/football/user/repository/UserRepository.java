package com.ssonsal.football.user.repository;

import com.ssonsal.football.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * 유저가 팀을 가지고 있는지 확인한다.
     *
     * @param userId 유저 아이디
     */
    boolean existsByIdAndTeamIsNull(Long userId);

    /**
     * 팀의 평균 나이값을 구한다.
     * 
     * @param teamId 팀 아이디
     * @return 팀 평균 나이값
     */
    @Query("SELECT ROUND(AVG(YEAR(CURRENT_DATE) - YEAR(u.birth)), 1) FROM com.ssonsal.football.user.entity.User u WHERE u.team.id = :teamId")
    Float findAverageAgeByTeamId(@Param("teamId") Long teamId);
}
