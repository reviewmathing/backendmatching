package com.hunko.missionmatching.core.presentation.dto;

import com.hunko.missionmatching.core.domain.Mission;
import com.hunko.missionmatching.core.domain.ReviewRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public record MissionPageDto(
        List<MissionDto> missions,
        MissionDto lastCursor
) {
    public static MissionPageDto from(List<Mission> missions, List<ReviewRequest> reviewRequests) {
        ArrayList<MissionDto> result = new ArrayList<>();
        for (Mission mission : missions) {
            Optional<ReviewRequest> any = reviewRequests.stream()
                    .filter(reviewRequest -> reviewRequest.getMissionId().equals(mission.getId())).findAny();
            MissionDto dto = MissionDtoMapper.toDto(mission,any.isPresent());
            result.add(dto);
        }
        if (missions.isEmpty()) {
            return new MissionPageDto(List.of(), null);
        }
        return new MissionPageDto(result, result.getLast());
    }
}
