package com.swmarastro.mykkumiserver.post.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class RegisterPostRequest {

    private Long subCategoryId;
    private String content;
    private List<PostImageDto> images;
}
