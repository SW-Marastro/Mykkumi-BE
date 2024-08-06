package com.swmarastro.mykkumiserver.post.dto;

import com.swmarastro.mykkumiserver.post.domain.PostImage;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Builder
@Getter
public class PostImageDto {

    private String url;
    private List<PinDto> pins;

    public static PostImageDto of(PostImage postImage) {
        List<PinDto> pinDtos = postImage.getPins().stream()
                .map(PinDto::of)
                .collect(Collectors.toList());

        return PostImageDto.builder()
                .url(postImage.getImageUrl())
                .pins(pinDtos)
                .build();
    }
}
