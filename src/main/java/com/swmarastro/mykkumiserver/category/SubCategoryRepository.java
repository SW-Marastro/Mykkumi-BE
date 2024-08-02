package com.swmarastro.mykkumiserver.category;

import com.swmarastro.mykkumiserver.category.domain.SubCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubCategoryRepository extends JpaRepository<SubCategory, Long> {
}
