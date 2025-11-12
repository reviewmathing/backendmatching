package com.hunko.missionmatching.core.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.hunko.missionmatching.core.exception.CoreException;
import com.hunko.missionmatching.helper.even.DomainEventUnitTest;
import java.time.LocalDateTime;
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
        LocalDateTime startDate = LocalDateTime.of(2020, 1, 1, 0, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2020, 1, 1, 23, 59, 59);
        assertThatCode(() -> new Mission("로또", new TimePeriod(startDate, endDate), creator)).doesNotThrowAnyException();
    }

    @ParameterizedTest
    @MethodSource("createFailArguments")
    void 미션생성실패(String title, LocalDateTime startDate, LocalDateTime endDate, Creator creator) {
        assertThatCode(() -> new Mission(title, new TimePeriod(startDate, endDate), creator)).isInstanceOf(
                CoreException.class);
    }

    @Test
    void 미션_ONGOING_업데이트(){
        Mission mission = TestMissionFactory.createMission(1L, MissionStatus.PENDING, 1L);
        LocalDateTime startDate = mission.getTimePeriod().getStartDate();

        mission.updateOngoing(startDate);

        assertThat(mission.getStatus()).isEqualTo(MissionStatus.ONGOING);
        MissionOngoinged event = getEvent(MissionOngoinged.class);
        assertThat(event.id()).isEqualTo(1L);
        assertThat(event.endDate()).isEqualTo(mission.getTimePeriod().getEndDate());
    }

    @Test
    void 미션_ONGOING_업데이트실패_시작일_불일치(){
        Mission mission = TestMissionFactory.createMission(1L, MissionStatus.PENDING, 1L);
        LocalDateTime startDate = mission.getTimePeriod().getStartDate().minusMinutes(1);

        assertThatThrownBy(() -> mission.updateOngoing(startDate)).isInstanceOf(CoreException.class);
    }

    @ParameterizedTest
    @EnumSource(names = {"COMPLETED","ONGOING"})
    void 미션_ONGOING_업데이트실패_상태가_PENDING이_아님(MissionStatus status){
        Mission mission = TestMissionFactory.createMission(1L, status, 1L);
        LocalDateTime startDate = mission.getTimePeriod().getStartDate();

        assertThatThrownBy(() -> mission.updateOngoing(startDate)).isInstanceOf(CoreException.class);
    }

    @Test
    void 미션_COMPLETED_업데이트(){
        Mission mission = TestMissionFactory.createMission(1L, MissionStatus.ONGOING, 1L);
        LocalDateTime endDate = mission.getTimePeriod().getEndDate();

        mission.updateCompleted(endDate);

        assertThat(mission.getStatus()).isEqualTo(MissionStatus.COMPLETED);
        MissionCompleted event = getEvent(MissionCompleted.class);
        assertThat(event.id()).isEqualTo(1L);
    }

    @Test
    void 미션_COMPLETED_업데이트실패_시작일_불일치(){
        Mission mission = TestMissionFactory.createMission(1L, MissionStatus.ONGOING, 1L);
        LocalDateTime endDate = mission.getTimePeriod().getEndDate().minusMinutes(1);

        assertThatThrownBy(() -> mission.updateCompleted(endDate)).isInstanceOf(CoreException.class);
    }

    @ParameterizedTest
    @EnumSource(names = {"COMPLETED","PENDING"})
    void 미션_COMPLETED_업데이트실패_상태가_PENDING이_아님(MissionStatus status){
        Mission mission = TestMissionFactory.createMission(1L, status, 1L);
        LocalDateTime endDate = mission.getTimePeriod().getEndDate();

        assertThatThrownBy(() -> mission.updateCompleted(endDate)).isInstanceOf(CoreException.class);
    }

    private static Stream<Arguments> createFailArguments() {
        String title = "test";
        LocalDateTime startDate = LocalDateTime.of(2020, 1, 1, 0, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2020, 1, 1, 23, 59, 59);
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
