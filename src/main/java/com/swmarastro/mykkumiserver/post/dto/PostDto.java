package com.swmarastro.mykkumiserver.post.dto;

import com.swmarastro.mykkumiserver.post.domain.Post;
import com.swmarastro.mykkumiserver.post.richtext.PostContentObject;
import com.swmarastro.mykkumiserver.user.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Schema(description = "포스트 하나 DTO")
@Getter
@Builder
public class PostDto {

    private Long id;
    private List<String> images;
    private String category;
    private String subCategory;
    private Writer writer;
    private List<PostContentObject> content;

    public static PostDto of(Post post, User writer) {
        return PostDto.builder()
                .id(post.getId())
                .images(post.getPostImageUrls())
                .subCategory(post.getSubCategory().getName())
                .category(post.getSubCategory().getCategory().getName()) //TODO 너무 .get.get인지 고민
                .content(post.getContent())
                .writer(Writer.of(writer))
                .build();
    }

    @Getter
    @Builder
    static class Writer {
        private String profileImage;
        private String nickname;

        static public Writer of(User user) {
            return Writer.builder()
                    .profileImage(user.getProfileImage())
                    .nickname(user.getNickname())
                    .build();
        }
    }

}
