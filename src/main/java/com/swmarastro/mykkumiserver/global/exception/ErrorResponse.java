package com.swmarastro.mykkumiserver.global.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {

    private String errorCode;
    private String message;
    private String detail;

    public static ErrorResponse from(CommonException e) {
        return ErrorResponse.builder()
                .errorCode(e.getErrorCodeName())
                .message(e.getMessage())
                .detail(e.getDetail())
                .build();
    }

    public static ErrorResponse from(Exception e) {
        return ErrorResponse.builder()
                .errorCode(ErrorCode.INTERNAL_SERVER_ERROR.getErrorCodeName())
                .message("알 수 없는 에러입니다. 잠시 후 시도해주세요.")
                .detail("서버 에러입니다.")
                .build();
    }
}
