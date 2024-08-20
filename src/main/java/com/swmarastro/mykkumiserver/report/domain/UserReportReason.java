package com.swmarastro.mykkumiserver.report.domain;

import com.swmarastro.mykkumiserver.global.exception.CommonException;
import com.swmarastro.mykkumiserver.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserReportReason {
    ETC("기타");

    private final String content;

    public static UserReportReason of(String reasonString) {
        try {
            return UserReportReason.valueOf(reasonString);
        } catch (IllegalArgumentException e) {
            throw new CommonException(ErrorCode.INVALID_VALUE, "해당 신고 사유가 존재하지 않습니다.", "해당 신고 사유 코드가 존재하지 않습니다.");
        }
    }
}
