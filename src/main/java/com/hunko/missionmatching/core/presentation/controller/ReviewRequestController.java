package com.hunko.missionmatching.core.presentation.controller;

import com.hunko.missionmatching.core.application.service.ReviewRequestService;
import com.hunko.missionmatching.core.domain.MissionId;
import com.hunko.missionmatching.core.domain.Requester;
import com.hunko.missionmatching.core.domain.ReviewRequestId;
import com.hunko.missionmatching.core.presentation.dto.IdDto;
import com.hunko.missionmatching.core.presentation.dto.ReviewRequestDto;
import com.hunko.missionmatching.core.presentation.dto.ReviewRequestResponseDto;
import com.hunko.missionmatching.core.presentation.dto.UpdateGithubUriDto;
import com.hunko.missionmatching.core.presentation.security.UserId;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/missions/{missionId}")
public class ReviewRequestController {

    private final ReviewRequestService reviewRequestService;

    @PreAuthorize("hasRole('USER')")
    @PostMapping
    public IdDto<Long> request(@PathVariable Long missionId, @UserId Long userId,
                               @RequestBody @Validated ReviewRequestDto reviewRequestDto) {
        Long request = reviewRequestService.request(Requester.of(userId), MissionId.of(missionId),
                reviewRequestDto.reviewCount());
        return new IdDto<>(request);
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/reviews/{reviewId}")
    public void updateGithubUri(@PathVariable Long missionId, @UserId Long userId, @PathVariable Long reviewId,
                                @RequestBody @Validated UpdateGithubUriDto updateGithubUriDto) {
        reviewRequestService.updateGithubUri(
                ReviewRequestId.of(reviewId),
                MissionId.of(missionId),
                Requester.of(userId),
                updateGithubUriDto.reviewCount(),
                updateGithubUriDto.toGithubUri()
        );
    }

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/reviews/{reviewId}")
    public void cancelReviewRequest(@PathVariable Long missionId, @UserId Long userId, @PathVariable Long reviewId) {
        reviewRequestService.cancel(
                ReviewRequestId.of(reviewId),
                MissionId.of(missionId),
                Requester.of(userId)
        );
    }
}
