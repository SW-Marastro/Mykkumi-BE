package com.swmarastro.mykkumiserver.post.service;

import com.swmarastro.mykkumiserver.global.config.S3properties;
import com.swmarastro.mykkumiserver.global.util.AwsS3Utils;
import com.swmarastro.mykkumiserver.post.dto.PostImagePreSignedUrlResponse;
import com.swmarastro.mykkumiserver.post.dto.ValidatePostImageUrlRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostImageService {

    private final AwsS3Utils awsS3Utils;
    private final S3properties s3properties;

    public PostImagePreSignedUrlResponse generatePostImagePreSignedUrl(String extension) {
        String filePath = s3properties.getPostImagePath() +
                UUID.randomUUID() +
                "." +
                extension;
        String presignedUrl = awsS3Utils.generatePreSignedUrl(filePath, s3properties.getBucket());
        String cdnUrl = s3properties.getCdnUrlPrefix()+filePath.substring("image/".length());
        return PostImagePreSignedUrlResponse.of(presignedUrl, cdnUrl);
    }



    public Boolean validatePostImageUrl(ValidatePostImageUrlRequest request) {
        return awsS3Utils.isValidUrl(request.getUrl(), request.getHashCode());
    }
}
