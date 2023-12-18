package com.ssonsal.football.game.dto.response;

import com.ssonsal.football.game.entity.Sub;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class SubsResponseDto {

    private Long userId;
    private String userName;
    private String nickName;
    private LocalDateTime createAt;

    public SubsResponseDto(Sub sub) {
        this.userId = sub.getUser().getId();
        this.userName = sub.getUser().getName();
        this.nickName = sub.getUser().getNickname();
        this.createAt = sub.getCreatedAt();
    }

}
