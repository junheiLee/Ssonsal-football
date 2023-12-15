package com.ssonsal.football.admin.dto.request;

import com.ssonsal.football.user.entity.User;
import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    private Long id;
    private String name;
    private String nickname;
    private String gender;
    private LocalDateTime createdAt;
    private Integer role;
    private Integer age;

    @Builder
    public UserDTO(Long id, String name, String nickname, String gender, LocalDateTime createdAt, Integer role) {
        this.id = id;
        this.name = name;
        this.nickname = nickname;
        this.gender = gender;
        this.createdAt = createdAt;
        this.role = role;
    }

    //엔티티에 없는 변수들
    public UserDTO build() {
        UserDTO userDTO = new UserDTO();
        userDTO.setAge(age);
        return userDTO;
    }

    public static UserDTO userFactory(User userdata) {
        return UserDTO.builder()
                .id(userdata.getId())
                .name(userdata.getName())
                .nickname(userdata.getNickname())
                .gender(userdata.getGender())
                .createdAt(userdata.getCreatedAt())
                .role(userdata.getRole())
                .build();
    }



}