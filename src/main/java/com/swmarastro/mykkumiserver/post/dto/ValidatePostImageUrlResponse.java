package com.swmarastro.mykkumiserver.post.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ValidatePostImageUrlResponse {

    private Boolean isValid;

    public static ValidatePostImageUrlResponse of(boolean isValid) {
        return ValidatePostImageUrlResponse.builder()
                .isValid(isValid)
                .build();
    }
}
