package com.swmarastro.mykkumiserver.report.domain;

import com.swmarastro.mykkumiserver.global.BaseEntity;
import com.swmarastro.mykkumiserver.user.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class UserReport extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_report_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "reporter_id")
    private User reporter;

    @Enumerated(EnumType.STRING)
    private UserReportReason reason;

    private String content;

    @Enumerated(EnumType.STRING)
    private ReportStatus status;

    private String result;

    public static UserReport of(User user, User reporter, String reason, String content) {
        return UserReport.builder()
                .user(user)
                .reporter(reporter)
                .reason(UserReportReason.of(reason))
                .content(content)
                .status(ReportStatus.RECEIVED)
                .build();
    }
}
