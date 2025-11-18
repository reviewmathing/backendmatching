package com.hunko.missionmatching.core.domain;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserValidator {

    private static final Integer MAX_REPORT_COUNT = 2;

    private final ReportReader reportReader;
    private final ReviewFailureReader reviewFailureReader;

    public boolean canRequestReview(Long userId) {
        List<ReviewFailure> failures = reviewFailureReader.readFrom(ReviewerId.of(userId));
        List<Report> reports = reportReader.loadApprovedReportFrom(TargetUserId.of(userId));
        return reports.size() + failures.size() < MAX_REPORT_COUNT;
    }
}
