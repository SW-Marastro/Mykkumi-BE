package com.swmarastro.mykkumiserver.like.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class LikesRequest {

    @NotNull
    private Long postId;
}
