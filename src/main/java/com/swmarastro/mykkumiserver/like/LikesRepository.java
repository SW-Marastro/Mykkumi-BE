package com.swmarastro.mykkumiserver.like;

import com.swmarastro.mykkumiserver.post.domain.Post;
import com.swmarastro.mykkumiserver.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface LikesRepository extends JpaRepository<Likes, Long> {

    @Query("SELECT l from Likes l WHERE l.post = :post AND l.user = :user")
    Optional<Likes> findByPostAndUser(Post post, User user); //TODO postid, userid로 하는게 나을지???
}
