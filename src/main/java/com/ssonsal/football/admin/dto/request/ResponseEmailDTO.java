package com.ssonsal.football.admin.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
public class ResponseEmailDTO {
    private String topicArn;
    private String subscriptionArn;
    private String userEmail;

    public ResponseEmailDTO(String topicArn, String subscriptionArn, String userEmail) {
        this.topicArn = topicArn;
        this.subscriptionArn = subscriptionArn;
        this.userEmail = userEmail;
    }

    public ResponseEmailDTO build() {
        ResponseEmailDTO responseEmailDTO = new ResponseEmailDTO();
        responseEmailDTO.setTopicArn(topicArn);
        responseEmailDTO.setSubscriptionArn(subscriptionArn);
        responseEmailDTO.setUserEmail(userEmail);
        return responseEmailDTO;
    }
}
