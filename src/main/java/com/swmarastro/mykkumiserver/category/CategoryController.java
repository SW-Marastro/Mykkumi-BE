package com.swmarastro.mykkumiserver.category;

import com.swmarastro.mykkumiserver.category.dto.CategoryDto;
import com.swmarastro.mykkumiserver.category.dto.CategoryListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/categories")
    public ResponseEntity<CategoryListResponse> getCategories() {
        List<CategoryDto> categories = categoryService.getCategories();
        CategoryListResponse categoryListResponse = CategoryListResponse.of(categories);
        return ResponseEntity.ok(categoryListResponse);
    }
}
