package com.swmarastro.mykkumiserver.report;

import com.swmarastro.mykkumiserver.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserReportRepository extends JpaRepository<UserReport, Long> {

    Boolean existsByReporterAndUser(User user, User reporter);
}
