package com.swmarastro.mykkumiserver.global.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.bind.MethodArgumentNotValidException;

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

    public static ErrorResponse from(MethodArgumentNotValidException e) {
        return ErrorResponse.builder()
                .errorCode(ErrorCode.INVALID_VALUE.getErrorCodeName())
                .message(e.getFieldError().getDefaultMessage())
                .detail(e.getFieldError().getField()+" '"+e.getFieldError().getRejectedValue()+"' 사용할 수 없습니다."+e.getFieldError().getDefaultMessage())
                .build();
    }
}
