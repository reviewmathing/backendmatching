package com.hunko.missionmatching.storage;

import com.hunko.missionmatching.core.domain.ReportStatus;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<ReportEntity, Long> {
    Page<ReportEntity> findAllByReportStatus(ReportStatus reportStatus, Pageable pageable);

    List<ReportEntity> findAllByTargetUserIdAndReportStatus(Long targetUserId, ReportStatus reportStatus);
}
