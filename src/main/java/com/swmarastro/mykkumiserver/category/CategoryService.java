
package com.swmarastro.mykkumiserver.category;

import com.swmarastro.mykkumiserver.category.domain.UserSubCategory;
import com.swmarastro.mykkumiserver.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final UserSubCategoryRepository userSubCategoryRepository;

    //회원가입 시 생성됨, 카테고리 초기 null 상태
    public UserSubCategory saveUserSubCategory(User user) {
        UserSubCategory userSubCategory = UserSubCategory.of(user);
        return userSubCategoryRepository.save(userSubCategory);
    }

}
