package com.swmarastro.mykkumiserver.report.dto;

import lombok.Getter;

@Getter
public class UserReportRequest {

    private String userUuid;
    private String reason;
    private String content;
}
