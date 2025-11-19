package com.hunko.missionmatching.core.application.service;

import com.hunko.missionmatching.core.domain.MissionId;
import com.hunko.missionmatching.core.domain.Report;
import com.hunko.missionmatching.core.domain.ReportFactory;
import com.hunko.missionmatching.core.domain.ReportReader;
import com.hunko.missionmatching.core.domain.ReportSaver;
import com.hunko.missionmatching.core.domain.ReportStatus;
import com.hunko.missionmatching.core.domain.ReviewAssignment;
import com.hunko.missionmatching.core.domain.ReviewAssignmentReader;
import com.hunko.missionmatching.core.domain.Reviewee;
import com.hunko.missionmatching.core.domain.RevieweeId;
import com.hunko.missionmatching.core.domain.ReviewerId;
import com.hunko.missionmatching.core.domain.User;
import com.hunko.missionmatching.core.domain.UserReader;
import com.hunko.missionmatching.core.exception.ErrorType;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final UserReader userReader;
    private final ReportReader reader;
    private final ReportSaver saver;
    private final ReviewAssignmentReader reviewAssignmentReader;

    @Transactional(readOnly = true)
    public Page<Report> readReport(int page, int limit, ReportStatus reportStatus) {
        return reader.read(PageRequest.of(page,limit,Sort.by(Direction.ASC,"id")), reportStatus);
    }

    public void report(MissionId missionId, Long reportUserId, ReviewerId reviewerId, String explanation) {
        ReviewAssignment reviewAssignment = reviewAssignmentReader.loadFrom(missionId, reviewerId)
                .orElseThrow(ErrorType.ENTITY_NOT_FOUND::toException);
        Reviewee reviewee = reviewAssignment.getReviewee(RevieweeId.of(reportUserId));
        List<User> users = userReader.loadFrom(List.of(reportUserId, reviewerId.toLong()));
        Report report = ReportFactory.create(users, missionId, reviewerId.toLong(), reportUserId,
                reviewee.getGithubUri(), explanation);
        saver.save(report);
    }

    public void reject(Long reportId){
        Report report = reader.loadFrom(reportId).orElseThrow(ErrorType.ENTITY_NOT_FOUND::toException);
        report.reject();
        saver.save(report);
    }

    public void approve(Long reportId){
        Report report = reader.loadFrom(reportId).orElseThrow(ErrorType.ENTITY_NOT_FOUND::toException);
        report.approve();
        saver.save(report);
    }
}
