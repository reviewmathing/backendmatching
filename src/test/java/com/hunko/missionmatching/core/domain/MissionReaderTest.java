package com.hunko.missionmatching.core.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.hunko.missionmatching.storage.MissionCursor;
import com.hunko.missionmatching.storage.MissionEntity;
import com.hunko.missionmatching.storage.MissionRepository;
import jakarta.persistence.EntityManager;
import java.time.ZonedDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class MissionReaderTest {

    @Autowired
    private MissionRepository missionRepository;
    @Autowired
    private EntityManager entityManager;

    @Test
    public void readAllMission() {
        ZonedDateTime now = ZonedDateTime.now();
        MissionEntity missionEntity1 = new MissionEntity(
                null,
                "test1",
                now.plusMinutes(10),
                now.plusMinutes(12),
                1L,
                MissionStatus.PENDING
        );
        MissionEntity missionEntity2 = new MissionEntity(
                null,
                "test2",
                now.minusDays(1),
                now.plusMinutes(2),
                1L,
                MissionStatus.ONGOING
        );
        MissionEntity missionEntity3 = new MissionEntity(
                null,
                "test3",
                now.minusDays(1),
                now.minusMinutes(1),
                1L,
                MissionStatus.COMPLETED
        );
        MissionEntity missionEntity4 = new MissionEntity(
                null,
                "test4",
                now.minusDays(1),
                now.plusMinutes(2),
                1L,
                MissionStatus.ONGOING
        );
        missionRepository.saveAll(List.of(missionEntity1, missionEntity2, missionEntity3, missionEntity4));

        MissionReader missionReader = new MissionReader(missionRepository);

        List<Mission> missions = missionReader.readAllMission(
                new MissionCursor(
                        missionEntity2.getId(),
                        missionEntity2.getStatus(),
                        missionEntity2.getStartDate(),
                        missionEntity2.getEndDate(),
                        2));

        List<String> names = missions.stream().map(Mission::getTitle).toList();

        assertThat(names).containsExactly("test4", "test1");
        assertThat(names).doesNotContain("test2");
    }
}
