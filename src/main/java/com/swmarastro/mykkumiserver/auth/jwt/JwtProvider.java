package com.swmarastro.mykkumiserver.auth.jwt;

import com.swmarastro.mykkumiserver.global.exception.CommonException;
import com.swmarastro.mykkumiserver.global.exception.ErrorCode;
import com.swmarastro.mykkumiserver.user.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.Duration;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

@Getter
@Component
@RequiredArgsConstructor
public class JwtProvider {

    private final JwtProperties jwtProperties;

    public String generateToken(User user, Duration duration, UUID uuid) {
        Date now = new Date();
        return makeToken(user, new Date(now.getTime() + duration.toMillis()),uuid);
    }

    public String generateToken(User user, Duration duration) {
        Date now = new Date();
        return makeToken(user, new Date(now.getTime() + duration.toMillis()));
    }

    /**
     * 토큰 생성 메서드 with token uuid
     */
    private String makeToken(User user, Date expiry, UUID uuid) {
        Date now = new Date();
        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setSubject(String.valueOf(user.getUuid())) //토큰이름: user uuid
                .setIssuedAt(now)
                .setExpiration(expiry)
                .claim("uuid", uuid.toString())
                .signWith(getSigningKey(jwtProperties.getSecretKey()), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * 토큰 생성 메서드 without token uuid
     */
    private String makeToken(User user, Date expiry) {
        Date now = new Date();
        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setSubject(String.valueOf(user.getUuid())) //토큰이름: user uuid
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(getSigningKey(jwtProperties.getSecretKey()), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * 유효한 토큰인지 검사하는 메서드
     */
    public boolean validToken(String token) {
        try {
            Key key = getSigningKey(jwtProperties.getSecretKey());
            getJwtParser()
                    .parseClaimsJws(token)
                    .getBody();
            return true;
        } catch (ExpiredJwtException e) {
            throw new CommonException(ErrorCode.TOKEN_EXPIRED, "만료된 토큰입니다.", "토큰 유효기간이 지났습니다.");
        } catch (Exception e) {
            throw new CommonException(ErrorCode.INVALID_TOKEN, "유효하지 않은 토큰입니다.", "유효하지 않은 토큰입니다.");
        }
    }

    /**
     * 토큰 subject 가져오기(user uuid)
     */
    public String getSubject(String token) {
        return getJwtParser().parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    /**
     * 토큰 만료시각 가져오기
     */
    public Date getExpiry(String token) {
        return getJwtParser().parseClaimsJws(token)
                .getBody()
                .getExpiration();
    }

    public String getUuidFromClaim(String token) {
        return getJwtParser().parseClaimsJws(token)
                .getBody().get("uuid").toString();
    }

    private JwtParser getJwtParser() {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey(jwtProperties.getSecretKey()))
                .build();
    }

    private Key getSigningKey(String key) {
        String encoded = Base64.getEncoder().encodeToString(key.getBytes());
        return Keys.hmacShaKeyFor(encoded.getBytes());
    }

}
