package com.swmarastro.mykkumiserver.post.service;

import com.swmarastro.mykkumiserver.global.config.S3properties;
import com.swmarastro.mykkumiserver.global.util.AwsS3Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostImageService {

    private final AwsS3Utils awsS3Utils;
    private final S3properties s3properties;

    public String generatePostImagePreSignedUrl(String extension) {
        String filePath = s3properties.getPostImagePath() +
                UUID.randomUUID() +
                "." +
                extension;
        return awsS3Utils.generatePreSignedUrl(filePath, s3properties.getBucket());
    }
}
