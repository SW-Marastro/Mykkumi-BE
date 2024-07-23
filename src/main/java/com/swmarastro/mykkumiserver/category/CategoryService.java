
package com.swmarastro.mykkumiserver.category;

import com.swmarastro.mykkumiserver.category.domain.Category;
import com.swmarastro.mykkumiserver.category.domain.SubCategory;
import com.swmarastro.mykkumiserver.category.domain.UserSubCategory;
import com.swmarastro.mykkumiserver.category.dto.CategoryDto;
import com.swmarastro.mykkumiserver.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final UserSubCategoryRepository userSubCategoryRepository;

    //회원가입 시 생성됨, 카테고리 초기 null 상태
    public UserSubCategory saveUserSubCategory(User user) {
        UserSubCategory userSubCategory = UserSubCategory.of(user);
        return userSubCategoryRepository.save(userSubCategory);
    }

    /**
     * 카테고리 전체 목록 조회 메서드
     */
    public List<CategoryDto> getCategories() {

        List<Category> allCategory = categoryRepository.findAll();
        // allCategory를 listOrder 기준으로 오름차순 정렬
        allCategory.sort(Comparator.comparing(Category::getListOrder));

        return allCategory.stream()
                .map(category -> {
                    List<SubCategory> subCategories = category.getSubCategories();
                    subCategories.sort(Comparator.comparing(SubCategory::getListOrder));
                    return CategoryDto.of(category, subCategories);
                })
                .collect(Collectors.toList());

    }

}
