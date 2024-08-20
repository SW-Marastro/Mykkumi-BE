package com.swmarastro.mykkumiserver.report.domain;

import lombok.Getter;

@Getter
public enum ReportStatus {
    RECEIVED,   // 신고접수됨
    PROCESSING, // 신고처리중
    APPROVED,   // 승인됨
    REJECTED    // 거절됨
}
