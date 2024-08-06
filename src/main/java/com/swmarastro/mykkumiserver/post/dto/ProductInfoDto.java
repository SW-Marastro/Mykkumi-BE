package com.swmarastro.mykkumiserver.post.dto;

import com.swmarastro.mykkumiserver.product.Product;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ProductInfoDto {

    private String name;
    private String url;

    public static ProductInfoDto of(Product product) {
        return ProductInfoDto.builder()
                .name(product.getProductName())
                .url(product.getProductUrl())
                .build();
    }
}
