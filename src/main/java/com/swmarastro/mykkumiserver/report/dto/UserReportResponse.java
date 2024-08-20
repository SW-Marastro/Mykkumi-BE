package com.swmarastro.mykkumiserver.report.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserReportResponse {

    private String result;

    public static UserReportResponse of(String result) {
        return UserReportResponse.builder()
                .result(result)
                .build();
    }
}
