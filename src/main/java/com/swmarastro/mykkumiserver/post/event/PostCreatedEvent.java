package com.swmarastro.mykkumiserver.post.event;

import com.swmarastro.mykkumiserver.category.domain.Category;
import com.swmarastro.mykkumiserver.post.domain.Post;
import com.swmarastro.mykkumiserver.post.dto.PostImageDto;
import com.swmarastro.mykkumiserver.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class PostCreatedEvent {

    private final Post post;
    private final Category category;
    private final User writer;
    private final List<PostImageDto> images;
}
