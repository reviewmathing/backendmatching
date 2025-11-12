package com.hunko.missionmatching.core.scheduler;

import static org.awaitility.Awaitility.await;

import com.hunko.missionmatching.core.domain.Mission;
import com.hunko.missionmatching.core.domain.MissionStatus;
import com.hunko.missionmatching.core.domain.TestMissionFactory;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;

class MissionSchedulerTest {

    @Test
    void 일정시간뒤에_핸들러실행() {
        LocalDateTime now = LocalDateTime.now();
        TestMissionScheduler testMissionScheduler = new TestMissionScheduler(now.plusMinutes(1));
        Mission mission = TestMissionFactory.createMission(1L, MissionStatus.PENDING, 1L);

        testMissionScheduler.schedule(mission);

        await()
                .atMost(1, TimeUnit.MINUTES)
                .until(() ->
                        testMissionScheduler.isHandleCall
                                && testMissionScheduler.missionId.equals(mission.getId().toLong())
                                && testMissionScheduler.handleTime.isEqual(now.plusMinutes(1))
                );
    }

    @Test
    void 일정이지나지_않으면_핸들리실행하지_않음() {
        LocalDateTime now = LocalDateTime.now();
        TestMissionScheduler testMissionScheduler = new TestMissionScheduler(now.plusMinutes(2));
        Mission mission = TestMissionFactory.createMission(1L, MissionStatus.PENDING, 1L);

        testMissionScheduler.schedule(mission);

        await()
                .atMost(1, TimeUnit.MINUTES)
                .until(() ->
                        !testMissionScheduler.isHandleCall
                );
    }


    private static class TestMissionScheduler extends MissionScheduler {

        private final LocalDateTime scheduleTime;

        private boolean isHandleCall = false;
        private Long missionId;
        private LocalDateTime handleTime;

        public TestMissionScheduler(LocalDateTime scheduleTime) {
            this.scheduleTime = scheduleTime;
        }

        @Override
        protected LocalDateTime getScheduleTime(Mission mission) {
            return scheduleTime;
        }

        @Override
        protected void handle(Long id, LocalDateTime time) {
            this.isHandleCall = true;
            this.missionId = id;
            this.handleTime = time;
        }
    }
}