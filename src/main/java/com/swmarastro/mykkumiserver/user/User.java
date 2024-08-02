package com.swmarastro.mykkumiserver.user;

import com.swmarastro.mykkumiserver.auth.OAuthProvider;
import com.swmarastro.mykkumiserver.category.domain.UserSubCategory;
import com.swmarastro.mykkumiserver.global.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Slf4j
@Getter
@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;
    @Column(updatable = false, nullable = false, unique = true, columnDefinition = "BINARY(16)")
    private UUID uuid;
    @Column(unique = true)
    private String nickname;
    @Column(nullable = false)
    private String email;
    private String introduction;
    private String profileImage;

    @Enumerated(value = EnumType.STRING)
    private OAuthProvider provider;

    @OneToOne(mappedBy = "user")
    private UserSubCategory userSubCategories;

    public static User of(OAuthProvider provider, String email) {
        return User.builder()
                .provider(provider)
                .email(email)
                .uuid(UUID.randomUUID())
                .build();
    }

    public void updateUser(String nickname, String introduction, String profileImage) {
        if (nickname != null) {
            this.nickname = nickname;
        }
        if (introduction != null) {
            this.introduction = introduction;
        }
        if(profileImage != null) {
            this.profileImage = profileImage;
        }
    }

}
