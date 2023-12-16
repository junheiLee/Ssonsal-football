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

    /**
     * AWS 자격 증명 제공자를 생성하여 반환한다
     * @param accessKeyID      AWS 액세스 키 ID
     * @param secretAccessKey  AWS 시크릿 액세스 키
     * @return AWS 자격 증명 제공자
     */

    public AwsCredentialsProvider getAwsCredentials(String accessKeyID, String secretAccessKey) {
        AwsBasicCredentials awsBasicCredentials = AwsBasicCredentials.create(accessKeyID, secretAccessKey);
        return () -> awsBasicCredentials;
    }


    /**
     * AWS Simple Notification Service (SNS) 클라이언트를 생성하여 반환한다
     * @return SNS 클라이언트
     */
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
