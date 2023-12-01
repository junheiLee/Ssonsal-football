package com.ssonsal.football.team.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TeamListDto {

    private Long id;

    private String name;

    private String preferredArea;

    private Integer ranking;

    private Float skillScore;

<<<<<<< HEAD
    private String ageAverage;
=======
    private Float ageAverage;
>>>>>>> 6f7de8312aa9059d359f72e01b9c134dc1e16bf4

    public TeamListDto(long id, String name, String preferredArea, float skillScore) {
        this.id = id;
        this.name = name;
        this.preferredArea = preferredArea;
        this.skillScore = skillScore;
    }
    
}