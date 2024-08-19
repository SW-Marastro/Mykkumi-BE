package com.swmarastro.mykkumiserver.user.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ProfileImagePreSignedUrlResponse {

    private String presignedUrl;
    private String cdnUrl;

    public static ProfileImagePreSignedUrlResponse of(String presignedUrl, String cdnUrl) {
        return ProfileImagePreSignedUrlResponse.builder()
                .presignedUrl(presignedUrl)
                .cdnUrl(cdnUrl)
                .build();
    }
}
