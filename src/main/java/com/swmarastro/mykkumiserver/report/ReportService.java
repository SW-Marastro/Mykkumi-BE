package com.swmarastro.mykkumiserver.report;

import com.swmarastro.mykkumiserver.global.exception.CommonException;
import com.swmarastro.mykkumiserver.global.exception.ErrorCode;
import com.swmarastro.mykkumiserver.report.dto.UserReportRequest;
import com.swmarastro.mykkumiserver.report.dto.UserReportResponse;
import com.swmarastro.mykkumiserver.user.User;
import com.swmarastro.mykkumiserver.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final UserReportRepository userReportRepository;
    private final UserRepository userRepository;


    public UserReportResponse reportUser(User reporter, UserReportRequest userReportRequest) {
        //TODO 유저가 탈퇴했을때 소프트 딜리트한다면 여기서 검색이 됨, 어떻게 할건지 결정하고 고치기
        User user = userRepository.findByUuid(UUID.fromString(userReportRequest.getUserUuid()))
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND, "해당 유저가 존재하지 않습니다.", "해당 uuid의 유저가 존재하지 않습니다."));

        //이미 신고한 유저인지 확인
        Boolean alreadyReported = userReportRepository.existsByReporterAndUser(user, reporter);

        //아니라면 신고 디비에 추가
        if (alreadyReported) {
            throw new CommonException(ErrorCode.DUPLICATE_REPORT, "이미 신고한 유저입니다.", "이미 신고한 유저입니다.");
        }
        UserReport userReport = UserReport.of(user, reporter, userReportRequest.getReason(), userReportRequest.getContent());
        userReportRepository.save(userReport);

        //응답 내려주기
        return UserReportResponse.of("신고가 접수되었습니다.");
    }

}