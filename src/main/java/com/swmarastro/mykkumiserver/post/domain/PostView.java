package com.swmarastro.mykkumiserver.post.domain;

import com.swmarastro.mykkumiserver.category.domain.Category;
import com.swmarastro.mykkumiserver.post.dto.PostImageDto;
import com.swmarastro.mykkumiserver.post.richtext.PostContentObject;
import com.swmarastro.mykkumiserver.user.User;
import com.swmarastro.mykkumiserver.post.dto.Writer;
import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Getter
@Builder
@Document(collection="postview")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class PostView {

    @Id
    private Long id;
    private String category;
    private String subCategory;
    private Writer writer;
    private List<PostContentObject> content;
    private List<PostImageDto> images;

    public static PostView of(Post post, Category category, User writer, List<PostImageDto> images) {
        Writer postWriter = Writer.builder()
                .uuid(writer.getUuid().toString())
                .profileImage(writer.getProfileImage())
                .nickname(writer.getNickname())
                .build();

        return PostView.builder()
                .id(post.getId())
                .category(category.getName())
                .subCategory(post.getSubCategory().getName())
                .writer(postWriter)
                .content(post.getContent())
                .images(images)
                .build();
    }

}
