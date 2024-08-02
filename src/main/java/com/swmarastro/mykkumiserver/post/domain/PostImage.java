package com.swmarastro.mykkumiserver.post.domain;

import com.swmarastro.mykkumiserver.global.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class PostImage extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_image_id")
    private Long id;
    @Column(nullable = false)
    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    private Long orderList;

    @OneToMany(mappedBy = "postImage", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Pin> pins = new ArrayList<>();

    public static PostImage of(String url, Long orderList) {
        return PostImage.builder()
                .imageUrl(url)
                .orderList(orderList)
                .build();
    }

    public void addPins(List<Pin> pins) {
        this.pins = pins;
    }

    public void addPost(Post post) {
        this.post = post;
    }
}
