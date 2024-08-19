package com.swmarastro.mykkumiserver.post.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PostImagePreSignedUrlResponse {

    private String presignedUrl;
    private String cdnUrl;

    public static PostImagePreSignedUrlResponse of(String presignedUrl, String cdnUrl) {
        return PostImagePreSignedUrlResponse.builder()
                .presignedUrl(presignedUrl)
                .cdnUrl(cdnUrl)
                .build();
    }
}
