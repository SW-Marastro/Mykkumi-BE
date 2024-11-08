package com.swmarastro.mykkumiserver.like;

import com.swmarastro.mykkumiserver.auth.annotation.Login;
import com.swmarastro.mykkumiserver.auth.annotation.RequiresLogin;
import com.swmarastro.mykkumiserver.like.dto.LikesRequest;
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
    public ResponseEntity<Void> like(@Login User user, @RequestBody LikesRequest request) {
        likeService.like(user, request.getPostId());
        return ResponseEntity.ok().build();
    }

    @RequiresLogin
    @PostMapping("/unlike")
    public ResponseEntity<Void> unlike(@Login User user, @RequestBody LikesRequest request) {
        likeService.unlike(user, request.getPostId());
        return ResponseEntity.ok().build();
    }
}
