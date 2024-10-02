package com.swmarastro.mykkumiserver.global.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class S3Config {

    private final AwsProperties awsProperties;

    @Bean
    public AmazonS3 AmazonS3Client() {
        AWSCredentials credentials = new BasicAWSCredentials(awsProperties.getAccessKey(), awsProperties.getSecretKey());

        return AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(awsProperties.getRegion())
                .build();
    }
}
