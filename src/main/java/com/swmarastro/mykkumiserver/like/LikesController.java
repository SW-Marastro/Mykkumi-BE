package com.swmarastro.mykkumiserver.like;

import com.swmarastro.mykkumiserver.auth.annotation.Login;
import com.swmarastro.mykkumiserver.auth.annotation.RequiresLogin;
import com.swmarastro.mykkumiserver.like.dto.LikesRequest;
import com.swmarastro.mykkumiserver.like.dto.LikesResponse;
import com.swmarastro.mykkumiserver.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/likes")
@RequiredArgsConstructor
public class LikesController {

    private final LikesService likeService;

    @RequiresLogin
    @PostMapping("/like")
    public ResponseEntity<LikesResponse> like(@Login User user, @RequestBody LikesRequest request) {
        LikesResponse response = LikesResponse.of(likeService.like(user, request.getPostId()));
        return ResponseEntity.ok(response);
    }

    @RequiresLogin
    @PostMapping("/unlike")
    public ResponseEntity<LikesResponse> unlike(@Login User user, @RequestBody LikesRequest request) {
        LikesResponse response = LikesResponse.of(likeService.unlike(user, request.getPostId()));
        return ResponseEntity.ok(response);
    }
}
