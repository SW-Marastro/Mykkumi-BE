package com.swmarastro.mykkumiserver.category.dto;

import com.swmarastro.mykkumiserver.category.domain.SubCategory;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class SubCategoryDto {

    private Long id;
    private String name;

    public static SubCategoryDto of(SubCategory subCategory) {
        return SubCategoryDto.builder()
                .id(subCategory.getId())
                .name(subCategory.getName())
                .build();
    }
}
