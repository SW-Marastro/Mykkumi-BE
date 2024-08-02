package com.swmarastro.mykkumiserver.post.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class RegisterPostResponse {

    private Long postId;

    public static RegisterPostResponse of(Long postId) {
        return RegisterPostResponse.builder()
                .postId(postId)
                .build();
    }
}
