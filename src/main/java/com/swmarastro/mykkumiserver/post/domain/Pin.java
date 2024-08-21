package com.swmarastro.mykkumiserver.post.domain;

import com.swmarastro.mykkumiserver.global.BaseEntity;
import com.swmarastro.mykkumiserver.product.Product;
import jakarta.persistence.*;
import lombok.*;

@Builder
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Pin extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pin_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_image_id", nullable = false)
    private PostImage postImage;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private Double positionX;

    @Column(nullable = false)
    private Double positionY;

    public static Pin of(PostImage postImage, Product product, Double positionX, Double positionY) {
        return Pin.builder()
                .postImage(postImage)
                .product(product)
                .positionX(positionX)
                .positionY(positionY)
                .build();
    }
}
