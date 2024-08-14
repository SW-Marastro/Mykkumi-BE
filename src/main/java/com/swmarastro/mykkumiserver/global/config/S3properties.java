package com.swmarastro.mykkumiserver.global.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Setter
@Getter
@Component
public class S3properties {
    @Value("${cloud.aws.credentials.access-key}")
    private String accessKey;

    @Value("${cloud.aws.credentials.secret-key}")
    private String secretKey;

    @Value("${cloud.aws.region.static}")
    private String region;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${aws.s3.post-image-path}")
    private String postImagePath;

    @Value("${aws.s3.profile-image-path}")
    private String profileImagePath;

    @Value("${aws.s3.s3_url_regex}")
    private String s3UrlRegex;

    @Value("${aws.s3.cdn_url_prefix}")
    private String cdnUrlPrefix;
}

