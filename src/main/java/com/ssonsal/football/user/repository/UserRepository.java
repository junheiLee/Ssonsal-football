package com.ssonsal.football.user.repository;

import com.ssonsal.football.team.dto.response.TeamMemberListDto;
import com.ssonsal.football.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {




}
