package com.swmarastro.mykkumiserver.report.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PostReportResponse {

    private String result;

    public static PostReportResponse of(String result) {
        return PostReportResponse.builder()
                .result(result)
                .build();
    }
}
