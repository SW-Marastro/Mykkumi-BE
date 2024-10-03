package com.swmarastro.mykkumiserver.like.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LikesResponse {

    private Boolean success;

    public static LikesResponse of(Boolean success) {
        return LikesResponse.builder()
                .success(success)
                .build();
    }
}
