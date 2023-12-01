package com.ssonsal.football.admin.repository;

import com.ssonsal.football.admin.dto.request.UserDTO;
import com.ssonsal.football.admin.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    // 생일 데이터을 나이로 변환
    @Query("SELECT YEAR(CURRENT_DATE) - YEAR(u.birth) FROM User u WHERE u.id = :userId")
    Integer calculateAgeByUserId(@Param("userId") Long userId);

    // 유저 리스트
    @Query("SELECT new com.ssonsal.joon.dto.request.UserDTO(t.id, t.name, t.nickname, t.gender, t.createdAt, t.role) FROM User t")
    List<UserDTO> findAllUser();


}
