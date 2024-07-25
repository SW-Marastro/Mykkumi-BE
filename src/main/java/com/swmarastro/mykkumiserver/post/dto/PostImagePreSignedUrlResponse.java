package com.swmarastro.mykkumiserver.post.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PostImagePreSignedUrlResponse {

    private String url;

    public static PostImagePreSignedUrlResponse of(String url) {
        return PostImagePreSignedUrlResponse.builder()
                .url(url)
                .build();
    }
}
