package com.hunko.missionmatching.core.service;

import static com.hunko.missionmatching.core.domain.TestMissionFactory.createMission;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.hunko.missionmatching.core.Authorities;
import com.hunko.missionmatching.core.application.service.MissionService;
import com.hunko.missionmatching.core.domain.Creator;
import com.hunko.missionmatching.core.domain.GithubUri;
import com.hunko.missionmatching.core.domain.Mission;
import com.hunko.missionmatching.core.domain.MissionCompleted;
import com.hunko.missionmatching.core.domain.MissionCreator;
import com.hunko.missionmatching.core.domain.MissionOngoinged;
import com.hunko.missionmatching.core.domain.MissionReader;
import com.hunko.missionmatching.core.domain.MissionRegistered;
import com.hunko.missionmatching.core.domain.MissionStatus;
import com.hunko.missionmatching.core.domain.TimePeriod;
import com.hunko.missionmatching.core.exception.CoreException;
import com.hunko.missionmatching.helper.FakeMissionReader;
import com.hunko.missionmatching.helper.FakeMissionSaver;
import com.hunko.missionmatching.helper.TestGithubUri;
import com.hunko.missionmatching.helper.even.DomainEventUnitTest;
import com.hunko.missionmatching.storage.MissionCursor;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;

class MissionServiceTest extends DomainEventUnitTest {

    @Test
    void 미션_생성() {
        MissionCreator missionCreator = new MissionCreator((any) -> true);
        FakeMissionSaver fakeMissionSaver = new FakeMissionSaver();
        MissionService missionService = new MissionService(missionCreator, fakeMissionSaver,
                new MissionReader(null));
        ZonedDateTime startDate = ZonedDateTime.of(LocalDateTime.of(2020, 1, 1, 0, 0, 0), ZoneId.systemDefault());
        ZonedDateTime endDate = ZonedDateTime.of(LocalDateTime.of(2020, 1, 1, 23, 59, 59), ZoneId.systemDefault());
        String title = "test";
        Creator creator = Creator.of(1L);
        GithubUri testUri = GithubUri.of("https://github.com/woowacourse-precourse/java-lotto-8");

        Long id = missionService.register(title, new TimePeriod(startDate, endDate), creator, testUri);

        Mission mission = fakeMissionSaver.getMission(Integer.parseInt(id.toString()));
        assertThat(mission).isNotNull();
        assertThat(mission.getTitle()).isEqualTo(title);
        assertThat(mission.getTimePeriod()).isEqualTo(new TimePeriod(startDate, endDate));
        assertThat(mission.getCreator()).isEqualTo(creator);
        MissionRegistered event = getEvent(MissionRegistered.class);
        assertThat(event).isNotNull();
    }

    @Test
    void 미션_생성_실패() {
        MissionCreator missionCreator = new MissionCreator((any) -> false);
        FakeMissionSaver fakeMissionSaver = new FakeMissionSaver();
        MissionService missionService = new MissionService(missionCreator, fakeMissionSaver,
                new MissionReader(null));
        ZonedDateTime startDate = ZonedDateTime.of(LocalDateTime.of(2020, 1, 1, 0, 0, 0), ZoneId.systemDefault());
        ZonedDateTime endDate = ZonedDateTime.of(LocalDateTime.of(2020, 1, 1, 23, 59, 59), ZoneId.systemDefault());
        String title = "test";
        Creator creator = Creator.of(1L);
        GithubUri testUri = GithubUri.of("https://github.com/woowacourse-precourse/java-lotto-8");

        assertThatThrownBy(
                () -> missionService.register(title, new TimePeriod(startDate, endDate), creator,
                        testUri)).isInstanceOf(
                CoreException.class);
    }

    @Test
    void 어드민조회시_모든상태를_조회() {
        FakeMissionReader fakeMissionReader = new FakeMissionReader();
        fakeMissionReader.addMission(
                createMission(1L, MissionStatus.ONGOING, 1L),
                createMission(2L, MissionStatus.ONGOING, 1L),
                createMission(3L, MissionStatus.PENDING, 1L),
                createMission(4L, MissionStatus.PENDING, 1L),
                createMission(5L, MissionStatus.COMPLETED, 1L)
        );
        MissionService missionService = new MissionService(null, null, fakeMissionReader);
        MissionCursor emptyRequest = MissionCursor.empty(7);

        List<Mission> missions = missionService.loadFrom(Authorities.ADMIN, emptyRequest);

        assertThat(missions).hasSize(5);
        assertThat(missions.stream().map(m -> m.getId().toLong())).containsExactly(1L, 2L, 3L, 4L, 5L);
    }

    @Test
    void 유저조회시_ONGOING상태를_조회() {
        FakeMissionReader fakeMissionReader = new FakeMissionReader();
        fakeMissionReader.addMission(
                createMission(1L, MissionStatus.ONGOING, 1L),
                createMission(2L, MissionStatus.ONGOING, 1L),
                createMission(3L, MissionStatus.PENDING, 1L),
                createMission(4L, MissionStatus.PENDING, 1L),
                createMission(5L, MissionStatus.COMPLETED, 1L)
        );
        MissionService missionService = new MissionService(null, null, fakeMissionReader);
        MissionCursor emptyRequest = MissionCursor.empty(7);

        List<Mission> missions = missionService.loadFrom(Authorities.USER, emptyRequest);

        assertThat(missions).hasSize(2);
        assertThat(missions.stream().map(m -> m.getId().toLong())).containsExactly(1L, 2L);
    }

    @Test
    void 미션_ONGOING으로_변경() {
        FakeMissionReader fakeMissionReader = new FakeMissionReader();
        Mission mission = createMission(1L, MissionStatus.PENDING, 1L);
        fakeMissionReader.addMission(mission);
        FakeMissionSaver fakeMissionSaver = new FakeMissionSaver();
        MissionService missionService = new MissionService(null, fakeMissionSaver, fakeMissionReader);

        missionService.updateOngoing(1L, mission.getTimePeriod().getServerStartDateTime());

        Mission savedMission = fakeMissionSaver.getMission(mission.getId().toLong().intValue());
        assertThat(savedMission.getStatus()).isEqualTo(MissionStatus.ONGOING);
        MissionOngoinged event = getEvent(MissionOngoinged.class);
        assertThat(event).isNotNull();
        assertThat(event.id()).isEqualTo(savedMission.getId().toLong());
    }

    @Test
    void 미션_COMPLETED으로_변경() {
        FakeMissionReader fakeMissionReader = new FakeMissionReader();
        Mission mission = createMission(1L, MissionStatus.ONGOING, 1L);
        fakeMissionReader.addMission(mission);
        FakeMissionSaver fakeMissionSaver = new FakeMissionSaver();
        MissionService missionService = new MissionService(null, fakeMissionSaver, fakeMissionReader);

        missionService.updateCompleted(1L, mission.getTimePeriod().getServerEndDateTime());

        Mission savedMission = fakeMissionSaver.getMission(mission.getId().toLong().intValue());
        assertThat(savedMission.getStatus()).isEqualTo(MissionStatus.COMPLETED);
        MissionCompleted event = getEvent(MissionCompleted.class);
        assertThat(event).isNotNull();
        assertThat(event.id()).isEqualTo(savedMission.getId().toLong());
    }

    @Test
    void 미션_수정_플로우검증() {
        FakeMissionReader fakeMissionReader = new FakeMissionReader();
        Mission mission = createMission(1L, MissionStatus.PENDING, 1L);
        fakeMissionReader.addMission(mission);
        FakeMissionSaver fakeMissionSaver = new FakeMissionSaver();
        MissionService missionService = new MissionService(null, fakeMissionSaver, fakeMissionReader);

        missionService.update(1L, "test1",new TimePeriod(ZonedDateTime.now(),ZonedDateTime.now().plusDays(1)),
                TestGithubUri.GITHUB_URI);

        Mission savedMission = fakeMissionSaver.getMission(mission.getId().toLong().intValue());
        assertThat(savedMission).isNotNull();
    }

    @Test
    void 미션_삭제_플로우검증() {
        FakeMissionReader fakeMissionReader = new FakeMissionReader();
        Mission mission = createMission(1L, MissionStatus.PENDING, 1L);
        fakeMissionReader.addMission(mission);
        FakeMissionSaver fakeMissionSaver = new FakeMissionSaver();
        MissionService missionService = new MissionService(null, fakeMissionSaver, fakeMissionReader);

        missionService.remove(1L);

        Mission savedMission = fakeMissionSaver.getMission(mission.getId().toLong().intValue());
        assertThat(savedMission).isNotNull();
    }
}
