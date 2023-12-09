package com.ssonsal.football.admin.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Alarm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String topicArn;
    private String subscriptionArn;
    private String userEmail;


    @Builder
    public Alarm(String topicArn, String subscriptionArn,String userEmail) {
        this.topicArn=topicArn;
        this.subscriptionArn = subscriptionArn;
        this.userEmail = userEmail;
    }
}
