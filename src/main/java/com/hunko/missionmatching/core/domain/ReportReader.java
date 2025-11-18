package com.hunko.missionmatching.core.domain;

import com.hunko.missionmatching.storage.ReportEntity;
import com.hunko.missionmatching.storage.ReportRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReportReader {

    private final ReportRepository reportRepository;

    public Page<Report> read(Pageable pageable, ReportStatus reportStatus) {
        Page<ReportEntity> reportEntities = reportRepository.findAllByReportStatus(reportStatus, pageable);
        return reportEntities.map(this::toReport);
    }

    public Optional<Report> loadFrom(Long reportId) {
        return reportRepository.findById(reportId).map(this::toReport);
    }

    private Report toReport(ReportEntity reportEntity) {
        return new Report(
                reportEntity.getId(),
                MissionId.of(reportEntity.getMissionId()),
                new Reporter(
                        reportEntity.getReporterUserId(),
                        reportEntity.getReporterUserName()
                ),
                new TargetUser(
                        reportEntity.getTargetUserId(),
                        reportEntity.getTargetUserName()
                ),
                GithubUri.of(reportEntity.getGithubUri()),
                reportEntity.getExplanation(),
                reportEntity.getReportStatus()
        );
    }
}
