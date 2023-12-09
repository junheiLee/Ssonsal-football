package com.ssonsal.football.admin.dto.request;

import com.ssonsal.football.admin.entity.Alarm;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@Builder
public class AlarmDTO {

    private Long id;
    private String topicArn;
    private String subscriptionArn;
    private String userEmail;

    public AlarmDTO(Long id, String topicArn, String subscriptionArn,String userEmail) {
        this.id=id;
        this.topicArn=topicArn;
        this.subscriptionArn = subscriptionArn;
        this.userEmail=userEmail;
    }

    public static AlarmDTO alarmFactory(Alarm alarm) {
        return AlarmDTO.builder()
                .id(alarm.getId())
                .topicArn(alarm.getTopicArn())
                .subscriptionArn(alarm.getSubscriptionArn())
                .userEmail(alarm.getUserEmail())
                .build();
    }

}
