package com.swmarastro.mykkumiserver.post.event.listener;

import com.swmarastro.mykkumiserver.post.PostMongoRepository;
import com.swmarastro.mykkumiserver.post.domain.PostView;
import com.swmarastro.mykkumiserver.post.event.PostCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class PostEventListener {

    private final PostMongoRepository postMongoRepository;

    @Async
    @TransactionalEventListener
    public void savePostView(PostCreatedEvent event) {
        //mongoDB에 저장하기
        PostView postView = PostView.of(event.getPost(), event.getCategory(), event.getWriter(), event.getImages());
        postMongoRepository.save(postView);

        log.info("[EVENT-PostCreatedEvent] Saved post view, Post ID : "+event.getPost().getId());
    }
}
