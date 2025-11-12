package com.hunko.missionmatching.core.presentation;

import com.hunko.missionmatching.core.domain.TimePeriod;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record MissionRegisterDto(
        @NotBlank String title,
        @NotNull LocalDateTime startDateTime,
        @NotNull LocalDateTime endDateTime
) {

    public TimePeriod timePeriod(){
        return new TimePeriod(startDateTime, endDateTime);
    }
}
