package com.hunko.missionmatching.core.scheduler;

import java.io.Serializable;
import java.time.LocalDateTime;

public record WalMissionDto(Long missionId, LocalDateTime time, QueueActionType queueActionType) implements
        Serializable {
}
