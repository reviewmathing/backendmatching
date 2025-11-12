package com.hunko.missionmatching.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import org.junit.jupiter.api.Test;

class DateUtilTest {

    @Test
    void equalOrBefore_같은날짜() {
        LocalDateTime date = LocalDateTime.of(2024, 1, 1, 10, 0);
        assertTrue(DateUtil.equalOrBefore(date, date));
    }

    @Test
    void equalOrBefore_1초차이() {
        LocalDateTime before = LocalDateTime.of(2024, 1, 1, 10, 0, 0);
        LocalDateTime after = LocalDateTime.of(2024, 1, 1, 10, 0, 1);
        assertTrue(DateUtil.equalOrBefore(before, after));
    }

    @Test
    void equalOrAfter_같은날짜() {
        LocalDateTime date = LocalDateTime.of(2024, 1, 1, 10, 0);
        assertTrue(DateUtil.equalOrAfter(date, date));
    }

    @Test
    void equalOrAfter_1초차이() {
        LocalDateTime after = LocalDateTime.of(2024, 1, 1, 10, 0, 1);
        LocalDateTime before = LocalDateTime.of(2024, 1, 1, 10, 0, 0);
        assertTrue(DateUtil.equalOrAfter(after, before));
    }

    @Test
    void 지역변경() {
        LocalDateTime localDateTime = LocalDateTime.of(2024, 1, 1, 10, 0);
        ZonedDateTime now = ZonedDateTime.of(localDateTime, ZoneId.systemDefault());
        ZonedDateTime newYork = now.withZoneSameInstant(ZoneId.of("America/New_York"));

        LocalDateTime serverDateTime = DateUtil.toServerDateTime(newYork);

        assertThat(localDateTime).isEqualTo(serverDateTime);
    }
}