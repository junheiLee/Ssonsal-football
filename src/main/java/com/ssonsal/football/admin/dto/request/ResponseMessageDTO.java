package com.ssonsal.football.admin.dto.request;


import com.ssonsal.football.game.entity.Game;
import com.ssonsal.football.user.entity.User;
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
    private User awayApplicantId;

    @Builder
    public ResponseMessageDTO(Long confirmedGameId, User awayApplicantId) {
        this.confirmedGameId = confirmedGameId;
        this.awayApplicantId = awayApplicantId;

    }

    public ResponseMessageDTO build() {
        ResponseMessageDTO responseMessageDTO = new ResponseMessageDTO();
        responseMessageDTO.setConfirmedGameId(confirmedGameId);

        return responseMessageDTO;
    }

    public static ResponseMessageDTO messageFactory(Game gameSMS) {

        return ResponseMessageDTO.builder()
                .awayApplicantId(gameSMS.getAwayApplicant())
                .build();

    }

}
