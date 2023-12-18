package com.ssonsal.football.admin.repository;


import com.ssonsal.football.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface UserManagementRepository extends JpaRepository<User, Long> {
    List<User> findByCreatedAtBetween(LocalDateTime localDateTime, LocalDateTime localDateTime1);
}
