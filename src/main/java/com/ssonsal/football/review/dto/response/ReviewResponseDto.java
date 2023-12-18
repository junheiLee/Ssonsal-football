package com.ssonsal.football.review.dto.response;

import com.ssonsal.football.review.entity.Review;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ReviewResponseDto {

    private Long id;
    private int reviewCode;
    private Long userId;
    private Long targetId;
    private Long gameId;
    private int skillScore;
    private int mannerScore;
    private String comment;
    private int deleteCode;

    public static ReviewResponseDto fromEntity(Review review) {
        return new ReviewResponseDtoBuilder()
                .id(review.getId())
                .reviewCode(review.getReviewCode())
                .userId(review.getUser().getId())
                .targetId(review.getTargetId())
                .gameId(review.getGame().getId())
                .skillScore(review.getSkillScore())
                .mannerScore(review.getMannerScore())
                .comment(review.getComment())
                .deleteCode(review.getDeleteCode())
                .build();
    }
}

