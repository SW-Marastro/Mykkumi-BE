package com.swmarastro.mykkumiserver.post.dto;

import com.swmarastro.mykkumiserver.post.domain.Post;
import com.swmarastro.mykkumiserver.post.richtext.PostContentObject;
import com.swmarastro.mykkumiserver.user.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Schema(description = "포스트 하나 DTO")
@Getter
@Builder
public class PostDto {

    private Long id;
    private String category;
    private String subCategory;
    private Writer writer;
    private List<PostContentObject> content;
    private List<PostImageDto> images;

    public static PostDto of(Post post, User writer) {

        List<PostImageDto> postImageDtos = post.getOrderedPostImages().stream()
                .map(PostImageDto::of)
                .collect(Collectors.toList());

        return PostDto.builder()
                .id(post.getId())
                .images(postImageDtos)
                .subCategory(post.getSubCategory().getName())
                .category(post.getSubCategory().getCategory().getName()) //TODO 너무 .get.get인지 고민
                .content(post.getContent())
                .writer(Writer.of(writer))
                .build();
    }

    @Getter
    @Builder
    static class Writer {
        private String uuid;
        private String profileImage;
        private String nickname;

        static public Writer of(User user) {
            return Writer.builder()
                    .uuid(user.getUuid().toString())
                    .profileImage(user.getProfileImage())
                    .nickname(user.getNickname())
                    .build();
        }
    }

}
