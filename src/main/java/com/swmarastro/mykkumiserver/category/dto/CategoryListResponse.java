package com.swmarastro.mykkumiserver.category.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class CategoryListResponse {
    private List<CategoryDto> categories;

    public static CategoryListResponse of(List<CategoryDto> categories) {
        return CategoryListResponse.builder()
                .categories(categories)
                .build();
    }
}
