package com.ssonsal.football.global.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class AWSConfig {

    @Value("${sns.topic.snsTopicARN}")
    private String snsTopicARN;

    @Value("${aws.awsAccessKey}")
    private String awsAccessKey;

    @Value("${aws.awsSecretKey}")
    private String awsSecretKey;

    @Value("${aws.awsRegion}")
    private String awsRegion;

}

