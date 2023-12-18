package com.ssonsal.football.admin.dto.response;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
public class ResponseMessageDTO {
    private Long confirmedGameId;

    @Builder
    public ResponseMessageDTO(Long confirmedGameId) {
        this.confirmedGameId = confirmedGameId;

    }

    public ResponseMessageDTO build() {
        ResponseMessageDTO responseMessageDTO = new ResponseMessageDTO();
        responseMessageDTO.setConfirmedGameId(confirmedGameId);

        return responseMessageDTO;
    }

}
