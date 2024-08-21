package com.swmarastro.mykkumiserver.auth.token;

import com.swmarastro.mykkumiserver.global.BaseEntity;
import com.swmarastro.mykkumiserver.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.UUID;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshToken extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "refresh_token_id", updatable = false, nullable = false)
    public Long id;

    @Column(updatable = false, unique = true, nullable = false)
    private UUID uuid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String refreshToken;
    @Column(nullable = false)
    private Date tokenExpiry;

    public static RefreshToken of(User user, String token, Date expiry, UUID uuid) {
        return RefreshToken.builder()
                .user(user)
                .refreshToken(token)
                .uuid(uuid)
                .tokenExpiry(expiry)
                .build();
    }
}
