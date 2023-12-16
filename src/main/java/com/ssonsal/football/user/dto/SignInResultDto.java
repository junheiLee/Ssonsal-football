package com.ssonsal.football.user.dto;

import com.ssonsal.football.user.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
import java.time.Period;

@Getter
@ToString
@NoArgsConstructor
public class SignInResultDto{

    private Long id;
    private Long teamId;
    private String email;
    private String name;
    private int age;
    private String gender;
    private String nickname;
    private String position;
    private String phone;
    private String intro;
    private String preferredArea;
    private String preferredTime;
    private int role;
    private Float skillScore;
    private Float mannerScore;
    private String accessToken;
    private String refreshToken;


    public SignInResultDto(User user,String accessToken,String refreshToken) {
        this.id = user.getId();
        this.teamId = (user.getTeam() != null) ? user.getTeam().getId() : 0L; // 혹은 다른 기본값으로 설정
        this.email = user.getEmail();
        this.name = user.getName();
        this.age = calculateAge(user.getBirth());
        this.gender = user.getGender();
        this.nickname = user.getNickname();
        this.position = user.getPosition();
        this.phone = user.getPhone();
        this.intro = user.getIntro();;
        this.preferredArea = user.getPreferredArea();
        this.preferredTime = user.getPreferredTime();
        this.skillScore = user.getSkillScore();
        this.mannerScore = user.getMannerScore();
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
    public static int calculateAge(LocalDate birth) {
        if (birth == null) {
            return 0; // 혹은 다른 기본값을 반환하도록 처리
        }
        LocalDate currentDate = LocalDate.now();
        return Period.between(birth, currentDate).getYears();
    }



}