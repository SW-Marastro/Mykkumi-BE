package com.swmarastro.mykkumiserver.post.dto;

import lombok.Getter;

@Getter
public class ValidatePostImageUrlRequest {

    private String hashCode;
    private String url;
}
