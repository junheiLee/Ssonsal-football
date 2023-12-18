package com.ssonsal.football.team.dto.request;

import com.ssonsal.football.team.entity.Team;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TeamCreateDto {

    @NotEmpty(message = "팀명은 필수 입력값입니다.")
    @Pattern(regexp = "^[a-zA-Z0-9가-힣]{2,12}$", message = "팀명은 2자리 이상, 12자리 이하여야 합니다.")
    private String name;

    private MultipartFile logo;

    private String preferredTime;

    private String preferredArea;

    private Long leaderId;

    private String intro;

    private Integer recruit;

    private Float skillScore;

    private Float mannerScore;

    public Team toEntity(TeamCreateDto teamCreateDto, String url, String key) {
        return Team.builder()
                .teamCreateDto(teamCreateDto)
                .logoUrl(url)
                .logoKey(key)
                .build();
    }

}
