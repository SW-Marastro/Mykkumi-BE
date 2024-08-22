package com.swmarastro.mykkumiserver.report.domain;

import com.swmarastro.mykkumiserver.post.domain.Post;
import com.swmarastro.mykkumiserver.user.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class PostReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_repost_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reporter_id", nullable = false)
    private User reporter;

    @Column(nullable = false, columnDefinition = "enum")
    @Enumerated(EnumType.STRING)
    private PostReportReason reason;

    private String content;

    @Column(nullable = false, columnDefinition = "enum")
    @Enumerated(EnumType.STRING)
    private ReportStatus status;

    private String result;

    public static PostReport of(User reporter, String reason, String content) {
        return PostReport.builder()
                .reporter(reporter)
                .reason(PostReportReason.of(reason))
                .content(content)
                .status(ReportStatus.RECEIVED)
                .build();
    }
}
