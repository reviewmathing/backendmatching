package com.hunko.missionmatching.core.presentation.controller;

import com.hunko.missionmatching.core.application.service.ReviewAssigmentService;
import com.hunko.missionmatching.core.application.service.UserService;
import com.hunko.missionmatching.core.domain.Mission;
import com.hunko.missionmatching.core.domain.MissionReader;
import com.hunko.missionmatching.core.domain.ReviewAssignment;
import com.hunko.missionmatching.core.domain.Reviewee;
import com.hunko.missionmatching.core.domain.RevieweeId;
import com.hunko.missionmatching.core.domain.User;
import com.hunko.missionmatching.core.presentation.dto.ReviewAssigmentDto;
import com.hunko.missionmatching.core.presentation.security.UserId;
import jakarta.validation.constraints.Min;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reviewassigments")
@RequiredArgsConstructor
public class ReviewAssignmentController {

    private final UserService userService;
    private final ReviewAssigmentService reviewAssigmentService;
    private final MissionReader missionReader;

    @PreAuthorize("hasRole('USER')")
    @GetMapping
    public ReviewAssigmentDto.ListView getList(@UserId Long userId,
                                               @Validated @RequestParam(value = "page", defaultValue = "0") @Min(0) Integer page) {
        Page<ReviewAssignment> reviewAssignmentPage = reviewAssigmentService.loadFrom(userId, page, 20);
        List<Mission> missions = missionReader.readByIds(
                reviewAssignmentPage.map(ReviewAssignment::getMissionId).toList());
        return new ReviewAssigmentDto.ListView(reviewAssignmentPage, missions);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("{reviewassigmentId}")
    public ReviewAssigmentDto.Details load(@UserId Long userId,
                                           @PathVariable("reviewassigmentId") Long reviewassigmentId) {
        ReviewAssignment reviewAssignment = reviewAssigmentService.loadFrom(userId, reviewassigmentId);
        String missionName = missionReader.readById(reviewAssignment.getMissionId().toLong()).map(Mission::getTitle)
                .orElseGet(() -> "Unknown");
        List<User> users = userService.loadFrom(
                reviewAssignment.getReviewee().stream().map(Reviewee::getRevieweeId).map(RevieweeId::toLong).toList());
        return ReviewAssigmentDto.Details.of(reviewAssignment, missionName, users);
    }
}
