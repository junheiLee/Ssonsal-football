package com.ssonsal.football.review.repository;

import com.ssonsal.football.review.etity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query("SELECT r FROM Review r WHERE (r.reviewCode = 0 OR r.reviewCode = 2) AND r.targetId = :teamId AND r.deleteCode = 0")
    List<Review> findReviewsByTeamId(@Param("teamId") Long teamId);

    @Query("SELECT r FROM Review r WHERE r.reviewCode = 1 AND r.targetId = :userId AND r.deleteCode = 0")
    List<Review> findReviewsByUserId(@Param("userId") Long userId);

    @Transactional
    @Modifying
    @Query("UPDATE Review r SET r.deleteCode = :deleteCode WHERE r.id = :reviewId")
    void updateDeleteCode(@Param("reviewId") Long reviewId, @Param("deleteCode") Integer deleteCode);

    @Query("SELECT r FROM Review r WHERE r.id = :reviewId")
    Review findReviewById(@Param("reviewId") Long reviewId);
}
