package com.swmarastro.mykkumiserver.auth.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.swmarastro.mykkumiserver.global.exception.CommonException;
import com.swmarastro.mykkumiserver.global.exception.ErrorCode;
import org.json.simple.parser.JSONParser;
import com.nimbusds.jwt.SignedJWT;
import com.nimbusds.jwt.JWTClaimsSet;
import com.swmarastro.mykkumiserver.auth.AppleProperties;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.json.simple.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.security.PrivateKey;
import java.security.Security;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class AppleService {

    private final JSONParser jsonParser = new JSONParser();
    private final ObjectMapper objectMapper = new ObjectMapper();

    private final AppleProperties appleProperties;

    public String getAppleInfo(String authorizationCode) {
        String clientSecret = generateClientSecret();
        String userId = "";
        String email  = "";
        String accessToken = "";

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-type", "application/x-www-form-urlencoded");

            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("grant_type"   , appleProperties.getGrantType());
            params.add("client_id"    , appleProperties.getClientId());
            params.add("client_secret", clientSecret);
            params.add("code"         , authorizationCode);
            params.add("redirect_uri" , "https://dev-api.mykkumi.com");

            RestTemplate restTemplate = new RestTemplate();
            HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(params, headers);

            ResponseEntity<String> response = restTemplate.exchange( //TODO 에러응답 넘어오는 거 받기
                    "https://appleid.apple.com/auth/token",
                    HttpMethod.POST,
                    httpEntity,
                    String.class
            ); //TODO object로 바로 받을 수 있는지 찾아보기

            JSONObject jsonObj = (JSONObject) jsonParser.parse(response.getBody());
            accessToken = String.valueOf(jsonObj.get("access_token"));

            //ID TOKEN을 통해 회원 고유 식별자 받기
            SignedJWT signedJWT = SignedJWT.parse(String.valueOf(jsonObj.get("id_token")));
            JWTClaimsSet getPayload = signedJWT.getJWTClaimsSet();

            String payloadString = objectMapper.writeValueAsString(getPayload.toJSONObject());
            JSONObject payload = objectMapper.readValue(payloadString, JSONObject.class);

            userId = String.valueOf(payload.get("sub"));
            email  = String.valueOf(payload.get("email"));
        } catch (Exception e) {
            e.printStackTrace();
            throw new CommonException(ErrorCode.INTERNAL_SERVER_ERROR, "로그인할 수 없습니다.", "애플 OAuth 정보를 가져오는 데 실패했습니다.");
            //TODO 클라이언트가 준 토큰이 잘못되었는지, 만에 하나 애플 인증 서버가 장애인지 애플에서 주는 코드로 구분해서 에러를 뿌려줘야 할 것 같다
        }

        AppleUserInfoDto appleDTO = AppleUserInfoDto.builder()
                .id(userId)
                .token(accessToken)
                .email(email).build();

        return appleDTO.getEmail();
    }


    /**
     * apple client secret 생성하는 메서드
     */
    private String generateClientSecret() {

        LocalDateTime expiration = LocalDateTime.now().plusMinutes(5);

        return Jwts.builder()
                .setHeaderParam(JwsHeader.KEY_ID, appleProperties.getKeyId())
                .setIssuer(appleProperties.getTeamId())
                .setAudience(appleProperties.getAudience())
                .setSubject(appleProperties.getClientId())
                .setExpiration(Date.from(expiration.atZone(ZoneId.systemDefault()).toInstant()))
                .setIssuedAt(new Date())
                .signWith(getPrivateKey(), SignatureAlgorithm.ES256)
                .compact();
    }

    /**
     * String에서 Private key로 변환하는 메서드
     */
    private PrivateKey getPrivateKey() {

        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        JcaPEMKeyConverter converter = new JcaPEMKeyConverter().setProvider("BC");

        try {
            byte[] privateKeyBytes = Base64.getDecoder().decode(appleProperties.getPrivateKey());
            PrivateKeyInfo privateKeyInfo = PrivateKeyInfo.getInstance(privateKeyBytes);
            return converter.getPrivateKey(privateKeyInfo);
        } catch (Exception e) {
            throw new CommonException(ErrorCode.INTERNAL_SERVER_ERROR, "로그인할 수 없습니다.", "String을 private key로 변환하는 데 실패했습니다.");
        }
    }
}
