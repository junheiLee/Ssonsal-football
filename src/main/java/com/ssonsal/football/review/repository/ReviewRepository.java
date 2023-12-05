package com.ssonsal.football.review.repository;

import com.ssonsal.football.review.etity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query("SELECT r FROM Review r WHERE (r.reviewCode = 0 OR r.reviewCode = 2) AND r.targetId = :teamId")
    List<Review> findReviewsByTeamId(@Param("teamId") Long teamId);

    @Query("SELECT r FROM Review r WHERE r.reviewCode = 1 AND r.targetId = :userId")
    List<Review> findReviewsByUserId(@Param("userId") Long userId);
}
