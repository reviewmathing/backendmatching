package com.hunko.missionmatching.core.presentation;

import com.hunko.missionmatching.core.domain.Mission;
import java.util.ArrayList;
import java.util.List;

public record MissionPageDto(
        List<MissionDto> missions,
        MissionDto lastCursor
) {
    public static MissionPageDto from(List<Mission> missions) {
        ArrayList<MissionDto> result = new ArrayList<>();
        for (Mission mission : missions) {
            MissionDto dto = MissionDtoMapper.toDto(mission);
            result.add(dto);
        }
        if(missions.isEmpty()) {
            return new MissionPageDto(List.of(), null);
        }
        return new MissionPageDto(result, result.getLast());
    }
}
