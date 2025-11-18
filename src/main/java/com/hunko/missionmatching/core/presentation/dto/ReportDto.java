package com.hunko.missionmatching.core.presentation.dto;

import com.hunko.missionmatching.core.domain.Report;
import com.hunko.missionmatching.core.domain.ReportStatus;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ReportDto {

    @Data
    public static class ReportItem{
        private Long id;

        private Long missionId;
        private Long targetUserId;
        private String targetUserName;

        private Long reporterUserId;
        private String reporterUserName;

        private String githubUri;
        private String explanation;

        private ReportStatus reportStatus;

        public static  ReportItem from(Report report){
            ReportItem reportItem = new ReportItem();
            reportItem.id = report.getId();
            reportItem.missionId = report.getMissionId().toLong();
            reportItem.targetUserId = report.getTargetUser().getId();
            reportItem.targetUserName = report.getTargetUser().getName();
            reportItem.reporterUserId = report.getReporter().getId();
            reportItem.reporterUserName = report.getReporter().getName();
            reportItem.githubUri = report.getGithubUri().toUriString();
            reportItem.explanation = report.getExplanation();
            reportItem.reportStatus = report.getReportStatus();
            return reportItem;
        }
    }

    @Data
    public static class Reporting{
        private Long missionId;
        private Long targetUserId;
        private String explanation;
    }
}
