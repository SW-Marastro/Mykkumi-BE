package com.swmarastro.mykkumiserver.user;

import com.swmarastro.mykkumiserver.auth.annotation.Login;
import com.swmarastro.mykkumiserver.auth.annotation.RequiresLogin;
import com.swmarastro.mykkumiserver.user.dto.MeResponse;
import com.swmarastro.mykkumiserver.user.dto.ProfileImagePreSignedUrlResponse;
import com.swmarastro.mykkumiserver.user.dto.UpdateUserRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final ProfileImageService profileImageService;

    @RequiresLogin
    @GetMapping("/users/me")
    public ResponseEntity<MeResponse> getMe(@Login User user) {
        MeResponse meResponse = userService.getMe(user);
        return ResponseEntity.ok(meResponse);
    }

    @RequiresLogin
    @PatchMapping("/users")
    public ResponseEntity<MeResponse> updateUser(@Login User loginUser, @Valid @RequestBody UpdateUserRequest updateUserRequest) {
        User user = userService.updateUser(loginUser, updateUserRequest);
        MeResponse meResponse = MeResponse.of(user);
        return ResponseEntity.ok(meResponse);
    }

    @RequiresLogin
    @GetMapping("/profileImage/preSignedUrl")
    public ResponseEntity<ProfileImagePreSignedUrlResponse> getProfileImagePresignedUrl(@RequestParam String extension) {
        String url = profileImageService.generatePostImagePreSignedUrl(extension);
        ProfileImagePreSignedUrlResponse response = ProfileImagePreSignedUrlResponse.of(url);
        return ResponseEntity.ok(response);
    }
}
