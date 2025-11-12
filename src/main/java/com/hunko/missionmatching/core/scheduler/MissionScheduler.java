package com.hunko.missionmatching.core.scheduler;

import com.hunko.missionmatching.core.domain.Mission;
import java.time.LocalDateTime;
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

    public MissionScheduler() {
        EXECUTOR.execute(()->{
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    SchedulerTask take = delayQueue.take();
                    handle(take.missionId,take.time);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }catch (Exception e){
                    log.error(e.getMessage(), e);
                }
            }
        });
    }

    public void schedule(Mission mission) {
        LocalDateTime time = getScheduleTime(mission);
        SchedulerTask schedulerTask = new SchedulerTask(time, mission.getId());
        delayQueue.add(schedulerTask);
    }

    protected abstract LocalDateTime getScheduleTime(Mission mission);

    protected abstract void handle(Long id,LocalDateTime time);

    private static class SchedulerTask implements Delayed {
        private final LocalDateTime time;
        private final Long missionId;

        public SchedulerTask(LocalDateTime time, Long missionId) {
            this.time = time;
            this.missionId = missionId;
        }

        @Override
        public long getDelay(TimeUnit unit) {
            long diffMillis = java.time.Duration.between(LocalDateTime.now(), time).toMinutes();
            return unit.convert(diffMillis, TimeUnit.MINUTES);
        }

        @Override
        public int compareTo(Delayed o) {
            return Long.compare(this.getDelay(TimeUnit.MINUTES), o.getDelay(TimeUnit.MINUTES));
        }
    }
}
