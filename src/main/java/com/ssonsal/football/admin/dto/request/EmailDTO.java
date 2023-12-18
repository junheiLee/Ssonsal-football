package com.ssonsal.football.admin.dto.request;

import com.ssonsal.football.user.entity.User;
import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@ToString
@NoArgsConstructor
public class EmailDTO {

    private Long id;                 // 보내는 회원 id
    private String email;            // 회원 이메일
    private String adminWrite;       // 보낸 관리자명
    private LocalDateTime createdAt; // 생성 날짜
    private int number;
    private String Text;
    private String phone;

    @Builder
    public EmailDTO(Long id, String email, String phone, LocalDateTime createdAt) {
        this.id = id;
        this.email = email;
        this.phone = phone;
        this.createdAt = createdAt;
    }


    // 엔티티에 없는 변수들
    public EmailDTO build() {
        EmailDTO emailDTO = new EmailDTO();
        emailDTO.setText(Text);
        emailDTO.setAdminWrite(adminWrite);
        emailDTO.setNumber(number);
        return emailDTO;
    }

    public static EmailDTO emailFactory(User user) {
        return EmailDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .createdAt(user.getCreatedAt())
                .phone(user.getPhone())
                .build();
    }

}

