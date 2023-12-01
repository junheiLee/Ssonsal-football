package com.ssonsal.football.team.repository;

import com.ssonsal.football.team.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team, Long> {
}
