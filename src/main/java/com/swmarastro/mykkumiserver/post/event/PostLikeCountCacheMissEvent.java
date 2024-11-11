package com.swmarastro.mykkumiserver.post.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PostLikeCountCacheMissEvent {

    private final Long postId;
    private final Long likeCount;
}
