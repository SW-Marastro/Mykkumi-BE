package com.swmarastro.mykkumiserver.post.dto;

import com.swmarastro.mykkumiserver.user.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Writer {
    private String uuid;
    private String profileImage;
    private String nickname;

    static public Writer of(User user) {
        return Writer.builder()
                .uuid(user.getUuid().toString())
                .profileImage(user.getProfileImage())
                .nickname(user.getNickname())
                .build();
    }
}
