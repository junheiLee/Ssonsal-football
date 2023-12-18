package com.ssonsal.football.admin.service;

import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.services.sns.SnsClient;

public interface CredentialService {

    /**
     * AWS 자격 증명 제공자를 생성하여 반환한다
     *
     * @param accessKeyID     AWS 액세스 키 ID
     * @param secretAccessKey AWS 시크릿 액세스 키
     * @return AWS 자격 증명 제공자
     */
    AwsCredentialsProvider getAwsCredentials(String accessKeyID, String secretAccessKey);

    /**
     * AWS Simple Notification Service (SNS) 클라이언트를 생성하여 반환한다
     *
     * @return SNS 클라이언트
     */
    SnsClient getSnsClient();
}
