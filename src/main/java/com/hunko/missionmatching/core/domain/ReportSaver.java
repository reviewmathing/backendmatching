package com.hunko.missionmatching.core.domain;

import com.hunko.missionmatching.storage.ReportEntity;
import com.hunko.missionmatching.storage.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReportSaver {

    private final ReportRepository reportRepository;

    public void save(Report report) {
        ReportEntity reportEntity = new ReportEntity(
                report.getId(),
                report.getMissionId().toLong(),
                report.getTargetUser().getId(),
                report.getTargetUser().getName(),
                report.getReporter().getId(),
                report.getReporter().getName(),
                report.getGithubUri().toUriString(),
                report.getExplanation(),
                report.getReportStatus()
        );
        reportRepository.save(reportEntity);
    }
}
