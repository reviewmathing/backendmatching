package com.hunko.missionmatching.core.application.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.hunko.missionmatching.core.domain.Mission;
import com.hunko.missionmatching.core.domain.MissionId;
import com.hunko.missionmatching.core.domain.MissionStatus;
import com.hunko.missionmatching.core.domain.TestMissionFactory;
import com.hunko.missionmatching.storage.MissionEntity;
import com.hunko.missionmatching.storage.MissionMapper;
import com.hunko.missionmatching.storage.MissionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MissionValidatorTest {

    @Autowired
    private MissionRepository missionRepository;

    @Test
    void 미션_검증_성공() {
        Mission mission = TestMissionFactory.createMission(null, MissionStatus.PENDING, 1L);
        MissionEntity save = missionRepository.save(MissionMapper.toMissionEntity(mission));
        MissionValidator missionValidator = new MissionValidator(missionRepository);

        boolean result = missionValidator.isInvalidMission(MissionId.of(save.getId()));

        assertThat(result).isTrue();
    }

    @Test
    void 미션_검증_실패() {
        MissionValidator missionValidator = new MissionValidator(missionRepository);

        boolean result = missionValidator.isInvalidMission(MissionId.of(1L));

        assertThat(result).isTrue();
    }
}