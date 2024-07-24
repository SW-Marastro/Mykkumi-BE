package com.swmarastro.mykkumiserver.post.service;

import com.swmarastro.mykkumiserver.global.exception.CommonException;
import com.swmarastro.mykkumiserver.global.exception.ErrorCode;
import com.swmarastro.mykkumiserver.global.util.Base64Utils;
import com.swmarastro.mykkumiserver.post.PostLatestCursor;
import com.swmarastro.mykkumiserver.post.PostRepository;
import com.swmarastro.mykkumiserver.post.domain.Post;
import com.swmarastro.mykkumiserver.post.dto.PostDto;
import com.swmarastro.mykkumiserver.post.dto.PostListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    //TODO 이름에 최신순이라고 알려줘야할것 같다. 고민해보고 이름 고치기
    public PostListResponse getInfiniteScrollPosts(String encodedCursor, Integer limit) {

        if (limit <= 0 || limit > 10)
            throw new CommonException(ErrorCode.INVALID_VALUE, "limit 값이 올바르지 않습니다.", "limit 값은 1<=limit<=10 이어야 합니다.");

        //TODO Cursor 인터페이스 하나 놓고 상속해서 커스텀하여 사용할 수도 있을 것 같다. 추후 구조 수정
        PostLatestCursor cursor = getCursorFromBase64String(encodedCursor);
        List<PostDto> posts = getPostsByCursorAndLimit(cursor, limit + 1);

        if (posts.size() < limit + 1) { //다음에 조회할 내용 없음
            return PostListResponse.end(posts);
        }
        posts.removeLast();
        Long lastId = getLastIdFromPostList(posts);
        PostLatestCursor nextCursor = PostLatestCursor.of(cursor.getStartedAt(), lastId);
        return PostListResponse.of(posts, Base64Utils.encode(nextCursor));
    }

    private PostLatestCursor getCursorFromBase64String(String encodedCursor) {
        if(encodedCursor==null || encodedCursor.isEmpty())
            return PostLatestCursor.of(LocalDateTime.now(), Long.MAX_VALUE);
        return Base64Utils.decode(encodedCursor, PostLatestCursor.class);
    }

    private List<PostDto> getPostsByCursorAndLimit(PostLatestCursor cursor, Integer limit) {
        List<Post> postList = postRepository.findLatestOrderByIdDesc(cursor.getPostId(), limit);
        return postList.stream()
                .map(post -> PostDto.of(post, post.getUser()))
                .collect(Collectors.toList());
    }

    private Long getLastIdFromPostList(List<PostDto> posts) {
        return posts.stream()
                .map(PostDto::getId)
                .min(Long::compareTo)
                .orElse(0L);
    }
}
