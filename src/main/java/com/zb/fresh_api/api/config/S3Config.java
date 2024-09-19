package com.zb.fresh_api.api.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class S3Config {

    @Value("${cloud.aws.credentials.accessKey}")
    private String accessKey;

    @Value("${cloud.aws.credentials.secretKey}")
    private String secretKey;

    @Value("${cloud.aws.region.static}")
    private String region;

    @Bean
    public S3Client s3Client() {
        final AwsBasicCredentials credentials = AwsBasicCredentials.create(accessKey, secretKey);
        final AwsCredentialsProvider provider = StaticCredentialsProvider.create(credentials);
        return S3Client.builder()
                .credentialsProvider(provider)
                .region(Region.of(region))
//                .region(Region.AP_NORTHEAST_2)
                .build();
    }

}
