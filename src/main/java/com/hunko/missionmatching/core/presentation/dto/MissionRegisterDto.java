package com.hunko.missionmatching.core.presentation.dto;

import com.hunko.missionmatching.core.domain.GithubUri;
import com.hunko.missionmatching.core.domain.TimePeriod;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.ZoneId;

public record MissionRegisterDto(
        @NotBlank String title,
        @NotNull LocalDateTime startDateTime,
        @NotNull LocalDateTime endDateTime,
        @NotNull ZoneId zone,
        @NotNull String github
) {

    public TimePeriod timePeriod() {
        return new TimePeriod(startDateTime.atZone(zone), endDateTime.atZone(zone));
    }

    public GithubUri githubUri() {
        return GithubUri.of(github);
    }
}
