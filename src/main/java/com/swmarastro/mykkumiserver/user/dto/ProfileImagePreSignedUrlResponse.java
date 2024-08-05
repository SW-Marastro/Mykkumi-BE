package com.swmarastro.mykkumiserver.user.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ProfileImagePreSignedUrlResponse {

    private String url;

    public static ProfileImagePreSignedUrlResponse of(String url) {
        return ProfileImagePreSignedUrlResponse.builder()
                .url(url)
                .build();
    }
}
