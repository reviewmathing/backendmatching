package com.hunko.missionmatching.core.presentation.controller;

import com.hunko.missionmatching.core.application.service.ReviewRequestReader;
import com.hunko.missionmatching.core.domain.Mission;
import com.hunko.missionmatching.core.domain.MissionReader;
import com.hunko.missionmatching.core.domain.Requester;
import com.hunko.missionmatching.core.domain.ReviewRequest;
import com.hunko.missionmatching.core.presentation.dto.ReviewRequestResponseDto;
import com.hunko.missionmatching.core.presentation.security.UserId;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/my/review-request")
@RestController
@RequiredArgsConstructor
public class MyReviewRequestController {


    private final ReviewRequestReader reviewRequestReader;
    private final MissionReader missionReader;

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public List<ReviewRequestResponseDto> findMyReviewRequests(@UserId Long userId) {
        List<ReviewRequest> reviewRequests = reviewRequestReader.loadFrom(Requester.of(userId));
        List<Mission> missions = missionReader.readByIds(
                reviewRequests.stream().map(ReviewRequest::getMissionId).toList());
        return reviewRequests.stream().map(r->ReviewRequestResponseDto.of(r,missions)).toList();
    }
}
