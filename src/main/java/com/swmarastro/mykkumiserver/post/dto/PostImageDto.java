package com.swmarastro.mykkumiserver.post.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class PostImageDto {

    private String url;
    private List<PinDto> pins;

}
