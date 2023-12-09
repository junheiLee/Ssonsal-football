package com.ssonsal.football.admin.service;

import com.ssonsal.football.admin.configuration.AWSConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;

@Service
@Slf4j
@RequiredArgsConstructor
public class CredentialService {

    private final AWSConfig awsConfig;

    public AwsCredentialsProvider getAwsCredentials(String accessKeyID, String secretAccessKey) {
        AwsBasicCredentials awsBasicCredentials = AwsBasicCredentials.create(accessKeyID, secretAccessKey);
        return () -> awsBasicCredentials;
    }

    public SnsClient getSnsClient() {

        log.info("AWS Access Key: {}", awsConfig.getAwsAccessKey());
        log.info("AWS Secret Key: {}", awsConfig.getAwsSecretKey());

        return SnsClient.builder()
                .credentialsProvider(
                        getAwsCredentials(awsConfig.getAwsAccessKey(), awsConfig.getAwsSecretKey())
                ).region(Region.of(awsConfig.getAwsRegion()))
                .build();

    }
}
