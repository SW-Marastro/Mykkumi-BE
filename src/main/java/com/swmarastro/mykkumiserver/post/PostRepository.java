package com.swmarastro.mykkumiserver.post;

import com.swmarastro.mykkumiserver.post.domain.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    //TODO Querydsl로 추후 변경하기
    @Query("SELECT p FROM Post p JOIN FETCH p.user WHERE p.id < :lastId AND p.isDeleted = false ORDER BY p.id DESC")
    List<Post> findLatestOrderByIdDesc(Long lastId, Pageable pageable);

    @Query("SELECT p FROM Post p JOIN FETCH p.user " +
            "WHERE p.id < :lastId AND p.isDeleted = false AND p.subCategory.id IN :categoryIds " +
            "ORDER BY p.id DESC")
    List<Post> findLatestOrderByIdDescInSubCategory(Long lastId, List<Long> categoryIds, Pageable pageable);


}
