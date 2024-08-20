package com.swmarastro.mykkumiserver.report;

import com.swmarastro.mykkumiserver.auth.annotation.Login;
import com.swmarastro.mykkumiserver.auth.annotation.RequiresLogin;
import com.swmarastro.mykkumiserver.report.dto.PostReportRequest;
import com.swmarastro.mykkumiserver.report.dto.PostReportResponse;
import com.swmarastro.mykkumiserver.report.dto.UserReportRequest;
import com.swmarastro.mykkumiserver.report.dto.UserReportResponse;
import com.swmarastro.mykkumiserver.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @RequiresLogin
    @PostMapping("/report/user")
    public ResponseEntity<UserReportResponse> reportUser(@Login User reporter, @RequestBody UserReportRequest userReportRequest) {
        UserReportResponse userReportResponse = reportService.reportUser(reporter, userReportRequest);
        return ResponseEntity.ok(userReportResponse);
    }

    @RequiresLogin
    @PostMapping("/report/post")
    public ResponseEntity<PostReportResponse> reportPost(@Login User reporter, @RequestBody PostReportRequest postReportRequest) {
        PostReportResponse postReportResponse = reportService.reportPost(reporter, postReportRequest);
        return ResponseEntity.ok(postReportResponse);
    }
}
