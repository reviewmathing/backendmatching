package com.hunko.missionmatching.core.presentation.controller;

import com.hunko.missionmatching.core.application.service.ReportService;
import com.hunko.missionmatching.core.domain.MissionId;
import com.hunko.missionmatching.core.domain.Report;
import com.hunko.missionmatching.core.domain.ReportStatus;
import com.hunko.missionmatching.core.domain.ReviewerId;
import com.hunko.missionmatching.core.presentation.dto.ReportDto.ReportItem;
import com.hunko.missionmatching.core.presentation.dto.ReportDto.Reporting;
import com.hunko.missionmatching.core.presentation.security.UserId;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportService reportService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public Page<ReportItem> readReport(@RequestParam(defaultValue = "0") @Min(0) int page,
                                       @RequestParam(defaultValue = "20") @Min(20) @Max(100) int limit,
                                       @NotNull @RequestParam(defaultValue = "PENDING") ReportStatus reportStatus) {
        Page<Report> reports = reportService.readReport(page, limit, reportStatus);
        return reports.map(ReportItem::from);
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping
    public void read(@UserId Long userId, @RequestBody Reporting reporting) {
        reportService.report(MissionId.of(reporting.getMissionId()), userId,
                ReviewerId.of(reporting.getTargetUserId()), reporting.getExplanation());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{reportId}/reject")
    public void reject(@PathVariable Long reportId) {
        reportService.reject(reportId);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{reportId}/approve")
    public void approve(@PathVariable Long reportId) {
        reportService.approve(reportId);
    }
}
