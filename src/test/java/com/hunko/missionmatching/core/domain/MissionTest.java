package com.hunko.missionmatching.core.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.hunko.missionmatching.core.exception.CoreException;
import com.hunko.missionmatching.helper.even.DomainEventUnitTest;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;

class MissionTest extends DomainEventUnitTest {

    @Test
    void 미션생성() {
        Creator creator = Creator.of(1L);
        ZonedDateTime startDate = ZonedDateTime.of(2020, 1, 1, 0, 0, 0, 0, ZoneId.systemDefault());
        ZonedDateTime endDate = ZonedDateTime.of(2020, 1, 1, 23, 59, 59, 0, ZoneId.systemDefault());
        GithubUri testUri = GithubUri.of("https://github.com/woowacourse-precourse/java-lotto-8");
        assertThatCode(() -> new Mission("로또", new TimePeriod(startDate, endDate), creator,
                testUri)).doesNotThrowAnyException();
    }

    @ParameterizedTest
    @MethodSource("createFailArguments")
    void 미션생성실패(String title, ZonedDateTime startDate, ZonedDateTime endDate, Creator creator) {
        GithubUri testUri = GithubUri.of("https://github.com/woowacourse-precourse/java-lotto-8");
        assertThatCode(() -> new Mission(title, new TimePeriod(startDate, endDate), creator, testUri)).isInstanceOf(
                CoreException.class);
    }

    @Test
    void 미션_ONGOING_업데이트() {
        Mission mission = TestMissionFactory.createMission(1L, MissionStatus.PENDING, 1L);
        LocalDateTime startDate = mission.getTimePeriod().getServerStartDateTime();

        mission.updateOngoing(startDate);

        assertThat(mission.getStatus()).isEqualTo(MissionStatus.ONGOING);
        MissionOngoinged event = getEvent(MissionOngoinged.class);
        assertThat(event.id()).isEqualTo(1L);
    }

    @Test
    void 미션_ONGOING_업데이트실패_시작일_불일치() {
        Mission mission = TestMissionFactory.createMission(1L, MissionStatus.PENDING, 1L);
        LocalDateTime startDate = mission.getTimePeriod().getServerStartDateTime().minusMinutes(1);

        assertThatThrownBy(() -> mission.updateOngoing(startDate)).isInstanceOf(CoreException.class);
    }

    @ParameterizedTest
    @EnumSource(names = {"COMPLETED", "ONGOING"})
    void 미션_ONGOING_업데이트실패_상태가_PENDING이_아님(MissionStatus status) {
        Mission mission = TestMissionFactory.createMission(1L, status, 1L);
        LocalDateTime startDate = mission.getTimePeriod().getServerStartDateTime();

        assertThatThrownBy(() -> mission.updateOngoing(startDate)).isInstanceOf(CoreException.class);
    }

    @Test
    void 미션_COMPLETED_업데이트() {
        Mission mission = TestMissionFactory.createMission(1L, MissionStatus.ONGOING, 1L);
        LocalDateTime endDate = mission.getTimePeriod().getServerEndDateTime();

        mission.updateCompleted(endDate);

        assertThat(mission.getStatus()).isEqualTo(MissionStatus.COMPLETED);
        MissionCompleted event = getEvent(MissionCompleted.class);
        assertThat(event.id()).isEqualTo(1L);
    }

    @Test
    void 미션_COMPLETED_업데이트실패_시작일_불일치() {
        Mission mission = TestMissionFactory.createMission(1L, MissionStatus.ONGOING, 1L);
        LocalDateTime endDate = mission.getTimePeriod().getServerEndDateTime().minusMinutes(1);

        assertThatThrownBy(() -> mission.updateCompleted(endDate)).isInstanceOf(CoreException.class);
    }

    @ParameterizedTest
    @EnumSource(names = {"COMPLETED", "PENDING"})
    void 미션_COMPLETED_업데이트실패_상태가_PENDING이_아님(MissionStatus status) {
        Mission mission = TestMissionFactory.createMission(1L, status, 1L);
        LocalDateTime endDate = mission.getTimePeriod().getServerEndDateTime();

        assertThatThrownBy(() -> mission.updateCompleted(endDate)).isInstanceOf(CoreException.class);
    }

    @Test
    void 미션수정() {
        Mission mission = new Mission("test1", new TimePeriod(ZonedDateTime.now(), ZonedDateTime.now().plusDays(1)),
                Creator.of(1L),
                GithubUri.of("https://github.com/woowacourse-precourse/java-lotto-8"));

        ZonedDateTime startDate = ZonedDateTime.now();
        ZonedDateTime endDate = ZonedDateTime.now().plusDays(2);
        String uri = "https://github.com/woowacourse-precourse/java-lotto-7";

        mission.update("test2", new TimePeriod(startDate, endDate), GithubUri.of(uri));

        assertThat(mission.getTitle()).isEqualTo("test2");
        assertThat(mission.getTimePeriod().getStartDate()).isEqualTo(startDate);
        assertThat(mission.getTimePeriod().getEndDate()).isEqualTo(endDate);
        assertThat(mission.getMissionUrl()).isEqualTo(GithubUri.of(uri));
    }

    @Test
    void 미션수정_실패() {
        Mission mission = new Mission(
                MissionId.emtpy(),
                "test1",
                new TimePeriod(ZonedDateTime.now(), ZonedDateTime.now().plusDays(1)),
                Creator.of(1L),
                MissionStatus.ONGOING,
                GithubUri.of("https://github.com/woowacourse-precourse/java-lotto-8")
        );

        ZonedDateTime startDate = ZonedDateTime.now();
        ZonedDateTime endDate = ZonedDateTime.now().plusDays(2);
        String uri = "https://github.com/woowacourse-precourse/java-lotto-7";

       assertThatThrownBy(() -> mission.update("test2", new TimePeriod(startDate, endDate), GithubUri.of(uri)))
               .isInstanceOf(CoreException.class);
    }

    @Test
    void 미션삭제() {
        Mission mission = new Mission("test1", new TimePeriod(ZonedDateTime.now(), ZonedDateTime.now().plusDays(1)),
                Creator.of(1L),
                GithubUri.of("https://github.com/woowacourse-precourse/java-lotto-8"));

        mission.delete();

        assertThat(mission.getStatus()).isEqualTo(MissionStatus.DELETED);
    }

    @Test
    void 미션수정_삭제() {
        Mission mission = new Mission(
                MissionId.emtpy(),
                "test1",
                new TimePeriod(ZonedDateTime.now(), ZonedDateTime.now().plusDays(1)),
                Creator.of(1L),
                MissionStatus.ONGOING,
                GithubUri.of("https://github.com/woowacourse-precourse/java-lotto-8")
        );

        assertThatThrownBy(() -> mission.delete())
                .isInstanceOf(CoreException.class);
    }

    private static Stream<Arguments> createFailArguments() {
        String title = "test";
        ZonedDateTime startDate = ZonedDateTime.of(2020, 1, 1, 0, 0, 0, 0, ZoneId.systemDefault());
        ZonedDateTime endDate = ZonedDateTime.of(2020, 1, 1, 23, 59, 59, 0, ZoneId.systemDefault());
        Creator creator = Creator.of(1L);
        return Stream.of(
                Arguments.of(null, startDate, endDate, creator),
                Arguments.of("", startDate, endDate, creator),
                Arguments.of(title, null, endDate, creator),
                Arguments.of(title, startDate, null, creator),
                Arguments.of(title, startDate, endDate, null)
        );
    }
}
