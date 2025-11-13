package com.hunko.missionmatching.core.presentation.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record ReviewRequestDto(@Min(1) @Max(5) Integer reviewCount) {
}
