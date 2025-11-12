package com.hunko.missionmatching.core.application.service;

import com.hunko.missionmatching.core.domain.MissionId;
import com.hunko.missionmatching.storage.MissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MissionValidator {

    private final MissionRepository missionRepository;

    public boolean isInvalidMission(MissionId missionId) {
        return missionRepository.findById(missionId.toLong()).isPresent();
    }
}
