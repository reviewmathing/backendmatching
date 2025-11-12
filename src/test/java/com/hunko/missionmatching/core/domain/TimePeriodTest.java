package com.hunko.missionmatching.core.domain;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.hunko.missionmatching.core.exception.CoreException;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import org.junit.jupiter.api.Test;

class TimePeriodTest {

    @Test
    void 단위기간_생성_성공() {
        ZonedDateTime startDate = ZonedDateTime.of(2020, Month.JANUARY.getValue(), 1, 1, 0, 0, 0,
                ZoneId.systemDefault());
        ZonedDateTime endDate = ZonedDateTime.of(2020, Month.JANUARY.getValue(), 1, 1, 0, 0, 0, ZoneId.systemDefault());
        assertThatCode(() -> new TimePeriod(startDate, endDate)).doesNotThrowAnyException();
    }

    @Test
    void 단위기간_생성_실패() {
        ZonedDateTime startDate = ZonedDateTime.of(2020, Month.JANUARY.getValue(), 1, 1, 0, 0, 0,
                ZoneId.systemDefault());
        ZonedDateTime endDate = ZonedDateTime.of(2020, Month.JANUARY.getValue(), 1, 0, 59, 59, 0,
                ZoneId.systemDefault());
        assertThrows(CoreException.class, () -> {
            new TimePeriod(startDate, endDate);
        });
    }

}