package com.ssonsal.football.rank.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@ToString
@Getter
@NoArgsConstructor
public class UpdatedRankDto {

    private Long modifiedBy;
    private LocalDateTime modifiedAt;

    public UpdatedRankDto(Long modifiedBy, LocalDateTime modifiedAt) {
        this.modifiedBy = modifiedBy;
        this.modifiedAt = modifiedAt;
    }
}
