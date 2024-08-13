package com.swmarastro.mykkumiserver.post;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PostLatestCursor {

    private LocalDateTime startedAt;
    private Long postId;

    public static PostLatestCursor of(LocalDateTime startedAt, Long postId) {
        return PostLatestCursor.builder()
                .postId(postId)
                .startedAt(startedAt)
                .build();
    }
}
