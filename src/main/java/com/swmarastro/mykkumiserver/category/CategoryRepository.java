package com.swmarastro.mykkumiserver.category;

import com.swmarastro.mykkumiserver.category.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
