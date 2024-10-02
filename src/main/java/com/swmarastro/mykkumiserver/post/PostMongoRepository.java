package com.swmarastro.mykkumiserver.post;

import com.swmarastro.mykkumiserver.post.domain.PostView;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PostMongoRepository extends MongoRepository<PostView, Long> {
}
