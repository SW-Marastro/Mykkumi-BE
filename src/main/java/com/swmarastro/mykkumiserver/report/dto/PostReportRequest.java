package com.swmarastro.mykkumiserver.report.dto;

import lombok.Getter;

@Getter
public class PostReportRequest {

    private Long postId;
    private String reason;
    private String content;
}
