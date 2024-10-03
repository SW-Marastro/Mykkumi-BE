package com.swmarastro.mykkumiserver.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    //400 BAD REQUEST
    ENCODING_ERROR(HttpStatus.BAD_REQUEST),
    DECODING_ERROR(HttpStatus.BAD_REQUEST),
    INVALID_VALUE(HttpStatus.BAD_REQUEST),
    NICKNAME_REQUIRED(HttpStatus.BAD_REQUEST),
    MISSING_REQUIRED_FIELD(HttpStatus.BAD_REQUEST),

    //401 UNAUTHORIZED
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED),
    AUTHENTICATION_REQUIRED(HttpStatus.UNAUTHORIZED),

    //403 FORBIDDEN
    ACCESS_DENIED(HttpStatus.FORBIDDEN),

    // 404 NOT FOUND
    NOT_FOUND(HttpStatus.NOT_FOUND),

    //409 CONFLICT
    DUPLICATE_VALUE(HttpStatus.CONFLICT),
    DUPLICATE_REPORT(HttpStatus.CONFLICT),
    ALREADY_EXISTS(HttpStatus.CONFLICT),

    //500 INTERNAL_SERVER_ERROR
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR),
    FILE_UPLOAD_ERROR(HttpStatus.INTERNAL_SERVER_ERROR),

    ;

    private final HttpStatus httpStatus;

    public String getErrorCodeName() {
        return this.name();
    }

}
