package com.swmarastro.mykkumiserver.like;

import com.swmarastro.mykkumiserver.global.BaseEntity;
import com.swmarastro.mykkumiserver.post.domain.Post;
import com.swmarastro.mykkumiserver.user.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Likes extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "likes_id")
    private Long id;

    private Boolean isDeleted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    public static Likes of(Post post, User user) {
        return Likes.builder()
                .post(post)
                .user(user)
                .isDeleted(false)
                .build();
    }

    public Boolean like() {
        isDeleted = false;
        return true;
    }

    public Boolean unlike() {
        isDeleted = true;
        return true;
    }
}
