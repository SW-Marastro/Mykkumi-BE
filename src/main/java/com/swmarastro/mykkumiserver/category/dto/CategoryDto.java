package com.swmarastro.mykkumiserver.category.dto;

import com.swmarastro.mykkumiserver.category.domain.Category;
import com.swmarastro.mykkumiserver.category.domain.SubCategory;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Builder
@Getter
public class CategoryDto {

    private Long id;
    private String name;
    private List<SubCategoryDto> subCategories;

    public static CategoryDto of(Category category, List<SubCategory> subCategories) {

        List<SubCategoryDto> subCategoriesDto = subCategories.stream()
                .map(SubCategoryDto::of)
                .collect(Collectors.toList());

        return CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .subCategories(subCategoriesDto)
                .build();
    }
}
