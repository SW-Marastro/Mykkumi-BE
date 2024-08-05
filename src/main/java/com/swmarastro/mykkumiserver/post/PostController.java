package com.swmarastro.mykkumiserver.post;

import com.swmarastro.mykkumiserver.auth.Login;
import com.swmarastro.mykkumiserver.post.dto.*;
import com.swmarastro.mykkumiserver.post.service.PostImageService;
import com.swmarastro.mykkumiserver.post.service.PostService;
import com.swmarastro.mykkumiserver.user.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "posts", description = "포스트 API")
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final PostImageService postImageService;

    /**
     * 홈화면 무한스크롤 포스트, 일단 모든 카테고리 최신순
     */
    @Operation(summary = "포스트 최신순 무한스크롤", description = "cursor와 limit로 무한스크롤 포스트를 불러옵니다." +
            "cursor와 limit 값은 null이어도 되며, cursor가 null이면 무한스크롤 시작입니다. limit 미입력시 기본값은 5입니다.")
    @GetMapping("/posts")
    public ResponseEntity<PostListResponse> getPosts(@RequestParam(required = false) String cursor, @RequestParam(required = false, defaultValue = "5") Integer limit) {
        PostListResponse infiniteScrollPosts = postService.getInfiniteScrollPosts(cursor, limit);
        return ResponseEntity.ok(infiniteScrollPosts);
    }

    @GetMapping("/posts/preSignedUrl")
    public ResponseEntity<PostImagePreSignedUrlResponse> getPostImagePreSignedUrl(@Login User user, @RequestParam String extension) {
        String url = postImageService.generatePostImagePreSignedUrl(extension);
        PostImagePreSignedUrlResponse postImagePreSignedUrlResponse = PostImagePreSignedUrlResponse.of(url);
        return ResponseEntity.ok(postImagePreSignedUrlResponse);
    }

    @PostMapping("/posts/imageUrl/validate")
    public ResponseEntity<ValidatePostImageUrlResponse> validatePostImageUrl(@RequestBody ValidatePostImageUrlRequest request) {
        Boolean isValid = postImageService.validatePostImageUrl(request);
        ValidatePostImageUrlResponse validatePostImageUrlResponse = ValidatePostImageUrlResponse.of(isValid);
        return ResponseEntity.ok(validatePostImageUrlResponse);
    }

    @PostMapping("/posts")
    public ResponseEntity<RegisterPostResponse> registerPost(@Login User user, @RequestBody RegisterPostRequest request) {
        Long savedPostId = postService.registerPost(user, request.getSubCategoryId(), request.getContent(), request.getImages());
        return ResponseEntity.ok().body(RegisterPostResponse.of(savedPostId));
    }

    @DeleteMapping("/posts/{postId}")
    public ResponseEntity<Void> deletePost(@Login User user, @PathVariable Long postId) {
        postService.deletePost(user, postId);
        return ResponseEntity.noContent().build();
    }
}
