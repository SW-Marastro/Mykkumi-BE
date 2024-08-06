package com.swmarastro.mykkumiserver.post.domain;

import com.swmarastro.mykkumiserver.category.domain.SubCategory;
import com.swmarastro.mykkumiserver.global.BaseEntity;
import com.swmarastro.mykkumiserver.post.richtext.PostContentObject;
import com.swmarastro.mykkumiserver.user.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;
    @Column(columnDefinition = "json")
    @JdbcTypeCode(SqlTypes.JSON)
    private List<PostContentObject> content;
    private Boolean isDeleted = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sub_category_id", nullable = false)
    private SubCategory subCategory;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostImage> postImages = new ArrayList<>();

    public List<PostImage> getOrderedPostImages() {
        return postImages.stream()
                .sorted(Comparator.comparing(PostImage::getOrderList))
                .collect(Collectors.toList());
    }

    public static Post of(List<PostContentObject> content, User user, SubCategory subCategory, List<PostImage> postImages) {
        Post post = Post.builder()
                .content(content)
                .user(user)
                .subCategory(subCategory)
                .postImages(postImages)
                .isDeleted(false)
                .build();

        postImages.forEach(postImage -> postImage.addPost(post)); // Post 설정

        return post;
    }

    public void deletePost() {
        this.isDeleted = true;
    }

}
