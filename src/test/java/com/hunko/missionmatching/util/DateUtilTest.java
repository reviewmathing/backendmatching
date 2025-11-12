package com.hunko.missionmatching.util;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
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
}