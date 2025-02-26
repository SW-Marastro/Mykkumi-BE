package com.swmarastro.mykkumiserver.post.service;

import com.swmarastro.mykkumiserver.category.CategoryService;
import com.swmarastro.mykkumiserver.category.domain.SubCategory;
import com.swmarastro.mykkumiserver.category.domain.UserSubCategory;
import com.swmarastro.mykkumiserver.global.exception.CommonException;
import com.swmarastro.mykkumiserver.global.exception.ErrorCode;
import com.swmarastro.mykkumiserver.global.util.Base64Utils;
import com.swmarastro.mykkumiserver.hashtag.*;
import com.swmarastro.mykkumiserver.post.PostLatestCursor;
import com.swmarastro.mykkumiserver.post.PostMongoRepository;
import com.swmarastro.mykkumiserver.post.PostRepository;
import com.swmarastro.mykkumiserver.post.domain.Pin;
import com.swmarastro.mykkumiserver.post.domain.Post;
import com.swmarastro.mykkumiserver.post.domain.PostImage;
import com.swmarastro.mykkumiserver.post.domain.PostView;
import com.swmarastro.mykkumiserver.post.dto.*;
import com.swmarastro.mykkumiserver.post.event.PostCreatedEvent;
import com.swmarastro.mykkumiserver.post.richtext.PostContentObject;
import com.swmarastro.mykkumiserver.post.richtext.RichTextUtils;
import com.swmarastro.mykkumiserver.product.Product;
import com.swmarastro.mykkumiserver.user.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class PostService {

    private final PostRepository postRepository;
    private final CategoryService categoryService;
    private final HashtagService hashtagService;
    private final PostMongoRepository postMongoRepository;
    private final ApplicationEventPublisher eventPublisher;

    public PostListResponse getInfiniteScrollPosts(User user, String encodedCursor, Integer limit) {
        if (limit <= 0 || limit > 10)
            throw new CommonException(ErrorCode.INVALID_VALUE, "limit 값이 올바르지 않습니다.", "limit 값은 1<=limit<=10 이어야 합니다.");
        List<Long> categoryIds = null;
        List<PostDto> posts = null;
        PostLatestCursor cursor = getCursorFromBase64String(encodedCursor);

        if (user == null || user.getUserSubCategories().getSubCategory().isEmpty()) {
            posts = getPostsByCursorAndLimit(cursor, limit + 1); //포스트 가져오기
        } else {
            UserSubCategory userSubCategories = user.getUserSubCategories();
            categoryIds = UserSubCategory.stringToList(userSubCategories.getSubCategory());
            posts = getPostsByCursorAndLimitAndUserCategory(cursor, limit + 1, categoryIds); //포스트 가져오기
        }

        if (posts.size() < limit + 1) { //다음에 조회할 내용 없음
            return PostListResponse.end(posts);
        }
        posts.removeLast();
        Long lastId = getLastIdFromPostList(posts);
        PostLatestCursor nextCursor = PostLatestCursor.of(cursor.getStartedAt(), lastId);
        return PostListResponse.of(posts, Base64Utils.encode(nextCursor));
    }

    public Long registerPost(User user, Long subCategoryId, String content, List<PostImageDto> images) {

        if (subCategoryId == null) {
            throw new CommonException(ErrorCode.MISSING_REQUIRED_FIELD, "카테고리를 입력해주세요.", "카테고리를 입력해주세요.");
        }

        //content -> rich text로 변환
        List<String> contentTexts = RichTextUtils.extractPlainTextAndHashtags(content);
        List<PostContentObject> postContentObjects = RichTextUtils.makePostContentStringToRichText(contentTexts);

        SubCategory subCategory = categoryService.getSubCategoryById(subCategoryId);

        List<PostImage> postImages = new ArrayList<>();
        Long order=1L;
        for (PostImageDto postImageDto : images) {
            String imageUrl = postImageDto.getUrl();
            PostImage postImage = PostImage.of(imageUrl, order++);
            List<Pin> pins = postImageDto.getPins().stream()
                    .map(pinDto -> {
                        Product product = Product.of(pinDto.getProductInfo().getName(), pinDto.getProductInfo().getUrl());
                        return Pin.of(postImage, product, pinDto.getPositionX(), pinDto.getPositionY());
                    })
                    .collect(Collectors.toList());
            postImage.addPins(pins);
            postImages.add(postImage);
        }

        Post post = Post.of(postContentObjects, user, subCategory, postImages);
        Post savedPost = postRepository.save(post); //mysql 저장 완료

        Hibernate.initialize(post.getSubCategory());
        Hibernate.initialize(post.getSubCategory().getCategory());

        eventPublisher.publishEvent(new PostCreatedEvent(post, subCategory.getCategory(), user, images));

        saveHashtags(contentTexts, savedPost);

        return savedPost.getId();
    }

    public void deletePost(User user, Long postId) {
        //본인의 포스트인지, 이미 삭제된 포스트는 아닌지 확인
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CommonException(ErrorCode.INVALID_VALUE, "존재하지 않는 게시글입니다.", "해당 id의 게시글이 존재하지 않습니다."));
        if(post.getUser()!=user)
            throw new CommonException(ErrorCode.ACCESS_DENIED, "본인이 작성한 게시글만 삭제 가능합니다.", "삭제 권한이 없습니다. 본인이 작성한 게시글만 삭제 가능합니다.");
        if(post.getIsDeleted())
            throw new CommonException(ErrorCode.INVALID_VALUE, "존재하지 않는 게시글입니다.", "해당 id의 게시글이 존재하지 않습니다.");

        //소프트 딜리트
        post.deletePost();
    }

    private PostLatestCursor  getCursorFromBase64String(String encodedCursor) {
        if(encodedCursor==null || encodedCursor.isEmpty())
            return PostLatestCursor.of(LocalDateTime.now(), Long.MAX_VALUE);
        return Base64Utils.decode(encodedCursor, PostLatestCursor.class);
    }

    private List<PostDto> getPostsByCursorAndLimitAndUserCategory(PostLatestCursor cursor, Integer limit, List<Long> categoryIds) {
        try {
            List<Long> postIdList = postRepository.findIdLatestOrderByIdDescInSubcategory(cursor.getPostId(), categoryIds, Pageable.ofSize(limit));
            List<PostView> postViews = postMongoRepository.findAllById(postIdList);

            // postIdList 순서대로 postViews 정렬
            postViews.sort(Comparator.comparing(postView -> postIdList.indexOf(postView.getId())));
            return postViews.stream().map(PostDto::of).collect(Collectors.toList());
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;

    }

    private List<PostDto> getPostsByCursorAndLimit(PostLatestCursor cursor, Integer limit) {
        try {
            List<Long> postIdList = postRepository.findIdLatestOrderByIdDesc(cursor.getPostId(), Pageable.ofSize(limit));
            List<PostView> postViews = postMongoRepository.findAllById(postIdList);

            // postIdList 순서대로 postViews 정렬
            postViews.sort(Comparator.comparing(postView -> postIdList.indexOf(postView.getId())));
            return postViews.stream().map(PostDto::of).collect(Collectors.toList());
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;

    }

    private Long getLastIdFromPostList(List<PostDto> posts) {
        return posts.stream()
                .map(PostDto::getId)
                .min(Long::compareTo)
                .orElse(0L);
    }

    private void saveHashtags(List<String> contentTexts, Post savedPost) {
        contentTexts.stream()
                .filter(contentText -> contentText.startsWith("#")) // 해시태그인 경우 필터링
                .map(contentText -> contentText.substring(1)) // 해시태그 이름만 추출
                .map(hashtagName -> {
                    Hashtag hashtag = hashtagService.findByName(hashtagName);
                    if (hashtag == null) {
                        hashtag = Hashtag.of(hashtagName);
                        hashtagService.saveHashtag(hashtag); // 새 해시태그 저장
                    }
                    return PostHashtag.of(savedPost, hashtag); // PostHashtag 생성
                })
                .forEach(hashtagService::savePostHashtag); // PostHashtag 저장
    }
}
