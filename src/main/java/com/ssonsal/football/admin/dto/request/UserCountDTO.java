package com.ssonsal.football.admin.dto.request;

import lombok.*;

@Setter
@Getter
@ToString
@NoArgsConstructor
public class UserCountDTO {
    private Long totalUserCount;
    private Long newUserCount;

    @Builder
    public UserCountDTO(Long totalUserCount, Long newUserCount) {
        this.totalUserCount = totalUserCount;
        this.newUserCount = newUserCount;
    }

    public UserCountDTO build() {
        UserCountDTO userCountDTO = new UserCountDTO();
        userCountDTO.setTotalUserCount(totalUserCount);
        userCountDTO.setNewUserCount(newUserCount);
        return userCountDTO;
    }

}
