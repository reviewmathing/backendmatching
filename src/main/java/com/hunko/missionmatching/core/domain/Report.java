package com.hunko.missionmatching.core.domain;

import lombok.Getter;

@Getter
public class Report {

    private final Long id;
    private final MissionId missionId;
    private final Reporter reporter;
    private final TargetUser targetUser;
    private final GithubUri githubUri;
    private final String explanation;
    private ReportStatus reportStatus;

    public Report(Long id, MissionId missionId, Reporter reporter, TargetUser targetUser, GithubUri githubUri,
                  String explanation,
                  ReportStatus reportStatus) {
        this.id = id;
        this.missionId = missionId;
        this.reporter = reporter;
        this.targetUser = targetUser;
        this.githubUri = githubUri;
        this.explanation = explanation;
        this.reportStatus = reportStatus;
    }

    public Report(Reporter reporter, MissionId missionId, TargetUser targetUser, GithubUri githubUri,
                  String explanation) {
        this(null, missionId, reporter, targetUser, githubUri, explanation, ReportStatus.PENDING);
    }

    public void reject() {
        this.reportStatus = ReportStatus.REJECTED;
    }

    public void approve() {
        this.reportStatus = ReportStatus.APPROVED;
    }
}
