package com.hunko.missionmatching.core.domain;

import com.hunko.missionmatching.storage.MissionEntity;
import com.hunko.missionmatching.storage.MissionMapper;
import com.hunko.missionmatching.storage.MissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MissionSaver {

    private final MissionRepository missionRepository;

    public Long save(Mission mission) {
        MissionEntity missionEntity = MissionMapper.toMissionEntity(mission);
        missionRepository.save(missionEntity);
        return missionEntity.getId();
    }
}
