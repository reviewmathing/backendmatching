package com.hunko.missionmatching.core.presentation.controller;

import com.hunko.missionmatching.core.Authorities;
import com.hunko.missionmatching.core.application.service.MissionService;
import com.hunko.missionmatching.core.application.service.ReviewRequestReader;
import com.hunko.missionmatching.core.domain.Creator;
import com.hunko.missionmatching.core.domain.Mission;
import com.hunko.missionmatching.core.domain.Requester;
import com.hunko.missionmatching.core.domain.ReviewRequest;
import com.hunko.missionmatching.core.presentation.dto.MissionCursorDto;
import com.hunko.missionmatching.core.presentation.dto.MissionPageDto;
import com.hunko.missionmatching.core.presentation.dto.MissionRegisterDto;
import com.hunko.missionmatching.core.presentation.security.UserId;
import com.hunko.missionmatching.core.presentation.security.UserRole;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/missions")
public class MissionController {

    private final MissionService missionService;
    private final ReviewRequestReader reviewRequestReader;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public Map<String, Long> register(@Validated @RequestBody MissionRegisterDto missionRegisterDto,
                                      @UserId Long userId) {
        Long id = missionService.register(missionRegisterDto.title(), missionRegisterDto.timePeriod(),
                Creator.of(userId), missionRegisterDto.githubUri());
        return Map.of("id", id);
    }

    @GetMapping
    public MissionPageDto findAll(@UserId Long userId, @UserRole Authorities authorities,
                                  @Validated MissionCursorDto cursorDto,
                                  @RequestParam(defaultValue = "10") @Min(10) @Max(50) Integer limit) {
        List<Mission> missions = missionService.loadFrom(authorities, cursorDto.toMissionCursor(limit));
        List<ReviewRequest> reviewRequests = reviewRequestReader.loadFrom(Requester.of(userId));
        return MissionPageDto.from(missions, reviewRequests);
    }
}
