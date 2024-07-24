package com.swmarastro.mykkumiserver.global.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.swmarastro.mykkumiserver.global.exception.CommonException;
import com.swmarastro.mykkumiserver.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;

import static java.util.Base64.*;

@RequiredArgsConstructor
public class Base64Utils {

    private static final Encoder encoder = getEncoder();
    private static final Decoder decoder = getDecoder();
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static String encode(Object obj) {
        try {
            objectMapper.registerModule(new JavaTimeModule());
            // 객체를 JSON 형식의 문자열로 변환
            String jsonString = objectMapper.writeValueAsString(obj);
            // JSON 문자열을 Base64로 인코딩
            return encoder.encodeToString(jsonString.getBytes());
        } catch (Exception e) {
            throw new CommonException(ErrorCode.ENCODING_ERROR, "Base64 인코딩 실패했습니다.", obj.getClass().getName()+" 값을 인코딩할 수 없습니다.");
        }
    }

    public static <T> T decode(String str, Class<T> clazz) {
        try {
            objectMapper.registerModule(new JavaTimeModule());
            // Base64 문자열을 디코딩하여 JSON 문자열로 변환
            String decodedString = new String(decoder.decode(str));
            // JSON 문자열을 객체로 변환
            return objectMapper.readValue(decodedString, clazz);
        } catch (Exception e) {
            throw new CommonException(ErrorCode.DECODING_ERROR, "Base64 디코딩 실패했습니다.", str+" 값을 디코딩할 수 없습니다.");
        }

    }

}
