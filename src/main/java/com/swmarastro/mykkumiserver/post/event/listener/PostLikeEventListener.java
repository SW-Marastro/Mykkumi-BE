package com.swmarastro.mykkumiserver.post.event.listener;

import com.swmarastro.mykkumiserver.global.util.RedisUtils;
import com.swmarastro.mykkumiserver.post.event.PostLikeCountCacheMissEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Slf4j
@Component
@RequiredArgsConstructor
public class PostLikeEventListener {

    private final RedisUtils redisUtils;
    private static final String POST_LIKE_KEY_PREFIX = "post:";
    private static final String POST_LIKE_KEY_SUFFIX = ":likes";
    private static final Duration POST_LIKE_DURATION = Duration.ofSeconds(30);

    @Async
    @EventListener
    public void setPostLikeCountCache(PostLikeCountCacheMissEvent postLikeCountCacheMissEvent) {
        String key = makePostLikeKey(postLikeCountCacheMissEvent.getPostId());
        redisUtils.setValues(key, postLikeCountCacheMissEvent.getLikeCount(), POST_LIKE_DURATION);
    }

    private String makePostLikeKey(Long postId) {
        return POST_LIKE_KEY_PREFIX + postId + POST_LIKE_KEY_SUFFIX;
    }
}
