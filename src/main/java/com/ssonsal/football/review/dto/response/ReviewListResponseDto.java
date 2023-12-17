package com.ssonsal.football.review.dto.response;

import com.ssonsal.football.review.entity.Review;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class ReviewListResponseDto {
    private Long reviewId;
    private String name;
    private Long userId;
    private Float mannerScore;
    private Float skillScore;
    private String comment;
    private LocalDate createdAt;

    public static ReviewListResponseDto fromEntity(Review review) {
        return new ReviewListResponseDtoBuilder()
                .reviewId(review.getId())
                .name(review.getUser().getName())
                .userId(review.getUser().getId())
                .mannerScore((float) review.getMannerScore())
                .skillScore((float) review.getSkillScore())
                .comment(review.getComment())
                .createdAt(LocalDate.from(review.getCreatedAt()))
                .build();
    }
}

