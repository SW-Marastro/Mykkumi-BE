package com.swmarastro.mykkumiserver.like;

import com.swmarastro.mykkumiserver.global.exception.CommonException;
import com.swmarastro.mykkumiserver.global.exception.ErrorCode;
import com.swmarastro.mykkumiserver.global.util.RedisUtils;
import com.swmarastro.mykkumiserver.post.PostRepository;
import com.swmarastro.mykkumiserver.post.domain.Post;
import com.swmarastro.mykkumiserver.post.event.PostLikeCountCacheMissEvent;
import com.swmarastro.mykkumiserver.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class LikesService {

    private final LikesRepository likeRepository;
    private final PostRepository postRepository;
    private final RedisUtils redisUtils;
    private final ApplicationEventPublisher eventPublisher;

    private static final String POST_LIKE_KEY_PREFIX = "post:";
    private static final String POST_LIKE_KEY_SUFFIX = ":likes";

    public Boolean like(User user, Long postId) {

        Post post = postRepository.findById(postId).orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND, "해당 포스트가 존재하지 않습니다.", "해당 포스트가 존재하지 않습니다."));

        //like가 테이블에 있는지 확인
        Optional<Likes> optionalLike = likeRepository.findByPostAndUser(post, user);
        //없음 -> 생성하고 좋아요 눌림
        if (optionalLike.isEmpty()) {
            Likes like = Likes.of(post, user);
            likeRepository.save(like);
            return true;
        }
        Likes like = optionalLike.get();
        //있는데 안눌린 상태 -> 좋아요 눌림
        if (like.getIsDeleted()) {
            return like.like();
        }
        //있는데 이미 눌린상태 -> 이미 좋아요 누른 포스트입니다.
        throw new CommonException(ErrorCode.ALREADY_EXISTS, "이미 좋아요를 누른 포스트입니다.", "이미 좋아요를 누른 포스트입니다.");
    }

    public Boolean unlike(User user, Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND, "해당 포스트가 존재하지 않습니다.", "해당 포스트가 존재하지 않습니다."));

        //like가 테이블에 있는지 확인
        Optional<Likes> optionalLike = likeRepository.findByPostAndUser(post, user);

        //없음 -> 좋아요를 취소할 수 없습니다.
        if (optionalLike.isEmpty()) {
            //TODO 이게 not_found가 맞는지 뭘쓸지 모르겠다
            throw new CommonException(ErrorCode.NOT_FOUND, "좋아요를 취소할 수 없습니다.", "좋아요를 취소할 수 없습니다.");
        }

        //있음 -> 좋아요 취소
        Likes likes = optionalLike.get();
        return likes.unlike();

        //TODO 좋아요가 이미 취소된 상태 -> 그냥 success 보내고 가만히 있으면 되지않나
    }

    public List<Boolean> isLikedByUser(User user, List<Long> postIds) {
        List<Map<String, Object>> results = likeRepository.findPostLikesStatus(postIds, user.getId());
        Map<Long, Boolean> likeStatusMap = results.stream()
                .collect(Collectors.toMap(
                        result -> ((Number) result.get("postId")).longValue(),
                        result -> ((Number) result.get("isLiked")).intValue() == 1 // 변환 로직 추가
                ));
        return postIds.stream()
                .map(postId -> likeStatusMap.getOrDefault(postId, false))
                .collect(Collectors.toList());
    }

    public Long getPostLikeCount(Long postId) {
        Long value = redisUtils.getLongValues(makePostLikeKey(postId));
        if (value != null) {
            return value;
        }
        //없으면 DB에서 조회
        Long postLikeCount = likeRepository.findPostLikeCountByPostId(postId);
        //redis 갱신하도록 이벤트 던지기
        eventPublisher.publishEvent(new PostLikeCountCacheMissEvent(postId, postLikeCount));
        return postLikeCount;
    }

    private String makePostLikeKey(Long postId) {
        return POST_LIKE_KEY_PREFIX + postId + POST_LIKE_KEY_SUFFIX;
    }

}
