package com.ssonsal.football.user.dto;

import com.ssonsal.football.user.entity.User;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.Period;

@Getter
@Setter
@ToString
public class ProfileResultDto {

    private Long id;

    private String email;

    private Long teamId;

    private String teamName;

    private String name;

    private int age;

    private String gender;

    private String nickname;

    private String position;

    private String phone;

    private String intro;

    private String preferredTime;

    private String preferredArea;

    private float skillScore;

    private float mannerScore;
    private int role;


    public ProfileResultDto(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.teamId = (user.getTeam() != null) ? user.getTeam().getId() : 0L; // 혹은 다른 기본값으로 설정
        this.teamName = (user.getTeam() != null) ? user.getTeam().getName() : "무소속"; // 혹은 다른 기본값으로 설정
        this.name = user.getName();
        this.age = calculateAge(user.getBirth());
        this.gender = user.getGender();
        this.nickname = user.getNickname();
        this.position = user.getPosition();
        this.phone = user.getPhone();
        this.intro = user.getIntro();
        this.preferredArea = user.getPreferredArea();
        this.preferredTime = user.getPreferredTime();
        this.skillScore = user.getSkillScore();
        this.mannerScore = user.getMannerScore();
        this.role=user.getRole();
    }

    public static int calculateAge(LocalDate birth) {
        if (birth == null) {
            return 0; // 혹은 다른 기본값을 반환하도록 처리
        }
        LocalDate currentDate = LocalDate.now();
        return Period.between(birth, currentDate).getYears();
    }
}
