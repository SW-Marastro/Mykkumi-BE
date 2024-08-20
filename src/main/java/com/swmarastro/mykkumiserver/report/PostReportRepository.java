package com.swmarastro.mykkumiserver.report;

import com.swmarastro.mykkumiserver.post.domain.Post;
import com.swmarastro.mykkumiserver.report.domain.PostReport;
import com.swmarastro.mykkumiserver.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostReportRepository extends JpaRepository<PostReport, Long> {

    Boolean existsByReporterAndPost(User user, Post post);
}
