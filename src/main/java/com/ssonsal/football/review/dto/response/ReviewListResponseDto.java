package com.ssonsal.football.review.dto.response;

import com.ssonsal.football.review.entity.Review;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ReviewListResponseDto {
    private Long reviewId;
    private String nickname;
    private Long userId;
    private Float mannerScore;
    private Float skillScore;
    private String comment;

    public static ReviewListResponseDto fromEntity(Review review) {
        return new ReviewListResponseDtoBuilder()
                .reviewId(review.getId())
                .nickname(review.getUser().getNickname())
                .userId(review.getUser().getId())
                .mannerScore((float) review.getMannerScore())
                .skillScore((float) review.getSkillScore())
                .comment(review.getComment())
                .build();
    }
}

