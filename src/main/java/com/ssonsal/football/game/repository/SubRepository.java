package com.ssonsal.football.game.repository;

import com.ssonsal.football.game.entity.Sub;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubRepository extends JpaRepository<Sub, Long> {

}