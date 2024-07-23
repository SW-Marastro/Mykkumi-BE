package com.swmarastro.mykkumiserver.user;

import com.swmarastro.mykkumiserver.auth.OAuthProvider;
import com.swmarastro.mykkumiserver.category.CategoryService;
import com.swmarastro.mykkumiserver.category.domain.UserSubCategory;
import com.swmarastro.mykkumiserver.category.UserSubCategoryRepository;
import com.swmarastro.mykkumiserver.global.exception.CommonException;
import com.swmarastro.mykkumiserver.global.exception.ErrorCode;
import com.swmarastro.mykkumiserver.global.util.AwsS3Utils;
import com.swmarastro.mykkumiserver.user.dto.UpdateUserRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final AwsS3Utils awsS3Utils;
    private final CategoryService categoryService;

    private final String PROFILE_IMAGE_PATH = "image/profileImage/";
    private final UserSubCategoryRepository userSubCategoryRepository;

    public User getUserByUuid(UUID uuid) {
        return userRepository.findByUuid(uuid)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND, "유저가 존재하지 않습니다.", "해당 uuid의 유저가 존재하지 않습니다."));
    }

    public User saveUser(OAuthProvider provider, String email) {
        User user = userRepository.save(User.of(provider, email));
        categoryService.saveUserSubCategory(user);//유저 생성 시 유저가 선택한 카테고리 레코드 자동생성
        return user;
    }

    public Optional<User> getUserByEmailAndProvider(String email, OAuthProvider provider) {
        return userRepository.findByEmailAndProvider(email, provider);
    }

    public User updateUser(User user, UpdateUserRequest updateUserRequest) {
        String nickname = updateUserRequest.getNickname();
        String introduction = updateUserRequest.getIntroduction();
        String imageUrl = null;
        List<Long> categoryIds = updateUserRequest.getCategoryIds();

        //중복된 닉네임일 때
        if (nickname != null && isNicknameExists(nickname)) {
            throw new CommonException(ErrorCode.DUPLICATE_VALUE, "이미 사용 중인 닉네임입니다.", "이미 사용 중인 닉네임입니다.");
        }

        //유저가 선택한 카테고리 update
        UserSubCategory userSubCategory = userSubCategoryRepository.findByUser(user);
        userSubCategory.updateSubCategory(categoryIds);

        //프로필 이미지 S3 업로드
        if (updateUserRequest.getProfileImage() != null) {
            imageUrl = uploadProfileImage(updateUserRequest.getProfileImage());
        }

        user.updateUser(nickname, introduction, imageUrl);
        return user;
    }

    /**
     * 닉네임 중복여부 체크
     */
    private boolean isNicknameExists(String nickname) {
        return userRepository.existsByNickname(nickname);
    }

    /**
     * S3 버킷에 이미지 업로드
     */
    private String uploadProfileImage(MultipartFile file) {
        String uploadUrl;
        //이미지 업로드
        try {
            System.out.println("image upload 시도한다");
            uploadUrl = awsS3Utils.upload(file, PROFILE_IMAGE_PATH);
        } catch (IOException e) {
            e.printStackTrace();
            throw new CommonException(ErrorCode.FILE_UPLOAD_ERROR, "파일 업로드에 실패했습니다.", "S3 버킷에 파일을 업로드하는 동안 오류가 발생했습니다.");
        }
        //업로드된 이미지 URL 받아오기
        String imageUrl = awsS3Utils.getImageUrl(uploadUrl);
        log.info("Success upload profile image "+imageUrl);
        return imageUrl;
    }

}
