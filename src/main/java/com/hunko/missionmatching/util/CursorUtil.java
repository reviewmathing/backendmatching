package com.hunko.missionmatching.util;

import com.hunko.missionmatching.core.domain.Mission;
import com.hunko.missionmatching.storage.MissionEntity;
import com.hunko.missionmatching.storage.MissionMapper;
import java.util.List;
import java.util.function.Supplier;

public class CursorUtil {
    public static List<Mission> fetchByCursor(Supplier<Boolean> isEmpty,
                                              Supplier<List<MissionEntity>> emptyProcessor,
                                              Supplier<List<MissionEntity>> nonEmptyProcessor) {
        if (isEmpty.get()) {
            List<MissionEntity> apply = emptyProcessor.get();
            return apply.stream().map(MissionMapper::toMission).toList();
        }
        List<MissionEntity> apply = nonEmptyProcessor.get();
        return apply.stream().map(MissionMapper::toMission).toList();
    }
}