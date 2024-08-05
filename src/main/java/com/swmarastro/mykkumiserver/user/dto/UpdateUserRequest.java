package com.swmarastro.mykkumiserver.user.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class UpdateUserRequest {

    @Pattern(regexp = "^[a-zA-Z0-9._\\-가-힣ㄱ-ㅎㅏ-ㅣ]+$", message = "닉네임은 숫자, 알파벳, '.', '_', '-', 한글만 포함할 수 있습니다.")
    @Size(min = 3, max = 16, message = "닉네임은 3자 이상 16자 이하이어야 합니다.")
    private String nickname;
    private String profileImage;
    private String introduction;
    private List<Long> categoryIds = new ArrayList<>();
}
