package com.swmarastro.mykkumiserver.global.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class S3properties {

    @Value("{$cloud.aws.credentials.access-key}")
    private String accessKey;

    @Value("{$cloud.aws.credentials.secret-key}")
    private String secretKey;

    @Value("{$cloud.aws.region}")
    private String region;

    @Value("{$cloud.aws.s3.bucket")
    private String bucket;
}
