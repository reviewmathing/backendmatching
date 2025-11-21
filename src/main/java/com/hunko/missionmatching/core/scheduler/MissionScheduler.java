package com.hunko.missionmatching.core.scheduler;

import com.hunko.missionmatching.core.domain.Mission;
import com.hunko.missionmatching.util.ServerTime;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class MissionScheduler {

    private static final ExecutorService EXECUTOR = Executors.newVirtualThreadPerTaskExecutor();
    private final DelayQueue<SchedulerTask> delayQueue = new DelayQueue<>();
    private final MissionSchedulerWal failMissionSchedulerWal;

    public MissionScheduler(MissionSchedulerWal failMissionSchedulerWal) {
        this.failMissionSchedulerWal = failMissionSchedulerWal;
        initQueue();

        EXECUTOR.execute(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    SchedulerTask take = delayQueue.take();
                    handle(take.missionId, take.time);
                    failMissionSchedulerWal.addLog(
                            new WalMissionDto(
                                    take.missionId,
                                    take.time,
                                    QueueActionType.POLL
                            )
                    );
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            }
        });
    }

    public void schedule(Mission mission) {
        LocalDateTime time = getScheduleTime(mission);
        SchedulerTask schedulerTask = new SchedulerTask(time, mission.getId().toLong());
        delayQueue.add(schedulerTask);
        failMissionSchedulerWal.addLog(
                new WalMissionDto(
                        schedulerTask.missionId,
                        schedulerTask.time,
                        QueueActionType.OFFER
                )
        );
    }

    protected abstract LocalDateTime getScheduleTime(Mission mission);

    protected abstract void handle(Long id, LocalDateTime time);

    private void initQueue() {
        List<WalMissionDto> walMissionDtos = failMissionSchedulerWal.readAll();
        Map<Long, LocalDateTime> missionMap = new HashMap<>();
        for (WalMissionDto walMissionDto : walMissionDtos) {
            if(walMissionDto.queueActionType().equals(QueueActionType.OFFER)){
                missionMap.put(walMissionDto.missionId(), walMissionDto.time());
                continue;
            }
            if (missionMap.containsKey(walMissionDto.missionId())) {
                missionMap.remove(walMissionDto.missionId());
            }
        }

        for (Entry<Long, LocalDateTime> longLocalDateTimeEntry : missionMap.entrySet()) {
            delayQueue.offer(new SchedulerTask(longLocalDateTimeEntry.getValue(), longLocalDateTimeEntry.getKey()));
        }
    }
    private static class SchedulerTask implements Delayed {

        private final LocalDateTime time;
        private final Long missionId;

        public SchedulerTask(LocalDateTime time, Long missionId) {
            this.time = time;
            this.missionId = missionId;
        }

        @Override
        public long getDelay(TimeUnit unit) {
            long diffMillis = java.time.Duration.between(ServerTime.now(), time).toMinutes();
            return unit.convert(diffMillis, TimeUnit.MINUTES);
        }

        @Override
        public int compareTo(Delayed o) {
            return Long.compare(this.getDelay(TimeUnit.MINUTES), o.getDelay(TimeUnit.MINUTES));
        }
    }
}
