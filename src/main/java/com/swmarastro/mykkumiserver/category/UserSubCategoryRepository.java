package com.swmarastro.mykkumiserver.category;

import com.swmarastro.mykkumiserver.category.domain.UserSubCategory;
import com.swmarastro.mykkumiserver.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserSubCategoryRepository extends JpaRepository<UserSubCategory, Long> {
    UserSubCategory findByUser(User user);
}
