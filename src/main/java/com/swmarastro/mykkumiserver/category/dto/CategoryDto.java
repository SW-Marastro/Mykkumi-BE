package com.swmarastro.mykkumiserver.category.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class CategoryDto {

    private Long id;
    private String name;
    private List<SubCategoryDto> subCategories;
}
