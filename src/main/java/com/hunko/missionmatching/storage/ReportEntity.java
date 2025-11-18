package com.hunko.missionmatching.storage;

import com.hunko.missionmatching.core.domain.ReportStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReportEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long missionId;
    private Long targetUserId;
    private String targetUserName;

    private Long reporterUserId;
    private String reporterUserName;

    private String githubUri;
    private String explanation;

    private ReportStatus reportStatus;

    public ReportEntity(Long id, Long missionId, Long targetUserId, String targetUserName, Long reporterUserId,
                        String reporterUserName, String githubUri, String explanation, ReportStatus reportStatus) {
        this.id = id;
        this.missionId = missionId;
        this.targetUserId = targetUserId;
        this.targetUserName = targetUserName;
        this.reporterUserId = reporterUserId;
        this.reporterUserName = reporterUserName;
        this.githubUri = githubUri;
        this.explanation = explanation;
        this.reportStatus = reportStatus;
    }
}
