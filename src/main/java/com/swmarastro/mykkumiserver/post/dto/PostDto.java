package com.swmarastro.mykkumiserver.post.dto;

import com.swmarastro.mykkumiserver.post.domain.Post;
import com.swmarastro.mykkumiserver.post.domain.PostView;
import com.swmarastro.mykkumiserver.post.richtext.PostContentObject;
import com.swmarastro.mykkumiserver.user.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Schema(description = "포스트 하나 DTO")
@Getter
@Builder
public class PostDto {

    @Id
    private Long id;
    private String category;
    private String subCategory;
    private Writer writer;
    private Long likeCount;
    private Boolean likedByCurrentUser;
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
                .category(post.getSubCategory().getCategory().getName())
                .content(post.getContent())
                .writer(Writer.of(writer))
                .build();
    }

    public static PostDto of(PostView postView) {
        return PostDto.builder()
                .id(postView.getId())
                .category(postView.getCategory())
                .subCategory(postView.getSubCategory())
                .writer(postView.getWriter())
                .content(postView.getContent())
                .images(postView.getImages())
                .build();
    }

    public void updateLikedNByCurrentUser(Boolean likedByCurrentUser) {
        this.likedByCurrentUser = likedByCurrentUser;
    }

    public void updateLikeCount(Long likeCount) {
        this.likeCount = likeCount;
    }

}
