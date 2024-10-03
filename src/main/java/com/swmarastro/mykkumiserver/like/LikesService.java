package com.swmarastro.mykkumiserver.like;

import com.swmarastro.mykkumiserver.global.exception.CommonException;
import com.swmarastro.mykkumiserver.global.exception.ErrorCode;
import com.swmarastro.mykkumiserver.post.domain.Post;
import com.swmarastro.mykkumiserver.post.service.PostService;
import com.swmarastro.mykkumiserver.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class LikesService {

    private final LikesRepository likeRepository;
    private final PostService postService;

    public Boolean like(User user, Long postId) {

        Post post = postService.findById(postId);

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
        Post post = postService.findById(postId);

        //like가 테이블에 있는지 확인
        Optional<Likes> optionalLike = likeRepository.findByPostAndUser(post, user);

        //없음 -> 좋아요를 취소할 수 없습니다.
        if(optionalLike.isEmpty()) {
            //TODO 이게 not_found가 맞는지 뭘쓸지 모르겠다
            throw new CommonException(ErrorCode.NOT_FOUND, "좋아요를 취소할 수 없습니다.", "좋아요를 취소할 수 없습니다.");
        }

        //있음 -> 좋아요 취소
        Likes likes = optionalLike.get();
        return likes.unlike();

        //TODO 좋아요가 이미 취소된 상태 -> 그냥 success 보내고 가만히 있으면 되지않나
    }

}
