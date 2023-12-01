package com.ssonsal.football.team.repository;


import com.ssonsal.football.team.entity.RejectId;
import com.ssonsal.football.team.entity.TeamReject;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRejectRepository extends JpaRepository<TeamReject, RejectId> {

}
