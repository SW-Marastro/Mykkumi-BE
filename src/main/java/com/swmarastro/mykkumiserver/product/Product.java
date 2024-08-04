package com.swmarastro.mykkumiserver.product;

import com.swmarastro.mykkumiserver.global.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Product extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id;

    private String productName;
    private String productUrl;

    public static Product of(String productName, String productUrl) {
        return Product.builder()
                .productName(productName)
                .productUrl(productUrl)
                .build();
    }
}
