package com.ssonsal.football.user.dto;

import com.ssonsal.football.user.entity.User;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ProfileResultDto {
    User user;
    public ProfileResultDto(User user){
        this.user = user;
    }
}
