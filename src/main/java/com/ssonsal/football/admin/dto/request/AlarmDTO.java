package com.ssonsal.football.admin.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@Builder
public class AlarmDTO {

    private String subscriptionArn;
    private String userEmail;

    public AlarmDTO(String subscriptionArn, String userEmail) {
        this.subscriptionArn = subscriptionArn;
        this.userEmail = userEmail;
    }


}
