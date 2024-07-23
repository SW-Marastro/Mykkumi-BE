package com.swmarastro.mykkumiserver.category.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class CategoryListResponse {
    private List<CategoryDto> categories;
}
