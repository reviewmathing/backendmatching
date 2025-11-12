package com.hunko.missionmatching.core.application.event;

import com.hunko.missionmatching.core.domain.MissionOngoinged;
import com.hunko.missionmatching.core.domain.MissionReader;
import com.hunko.missionmatching.core.domain.MissionRegistered;
import com.hunko.missionmatching.core.scheduler.MissionEndScheduler;
import com.hunko.missionmatching.core.scheduler.MissionStartedScheduler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class MissionListener {

    private final MissionReader missionReader;
    private final MissionStartedScheduler missionStartedScheduler;
    private final MissionEndScheduler missionEndScheduler;

    @TransactionalEventListener(value = MissionRegistered.class, phase = TransactionPhase.AFTER_COMMIT)
    public void handle(MissionRegistered event) {
        missionReader.readById(event.id()).ifPresent(missionStartedScheduler::schedule);
    }

    @TransactionalEventListener(value = MissionOngoinged.class, phase = TransactionPhase.AFTER_COMMIT)
    public void handle(MissionOngoinged event) {
        missionReader.readById(event.id()).ifPresent(missionEndScheduler::schedule);
    }
}
