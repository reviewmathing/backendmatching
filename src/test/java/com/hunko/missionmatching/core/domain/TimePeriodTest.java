package com.hunko.missionmatching.core.domain;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.hunko.missionmatching.core.exception.CoreException;
import java.time.LocalDateTime;
import java.time.Month;
import org.junit.jupiter.api.Test;

class TimePeriodTest {

    @Test
    void 단위기간_생성_성공() {
        LocalDateTime startDate = LocalDateTime.of(2020, Month.JANUARY, 1, 1, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2020, Month.JANUARY, 1, 1, 0, 0);
        assertThatCode(() -> new TimePeriod(startDate, endDate)).doesNotThrowAnyException();
    }

    @Test
    void 단위기간_생성_실패() {
        LocalDateTime startDate = LocalDateTime.of(2020, Month.JANUARY, 1, 1, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2020, Month.JANUARY, 1, 0, 59, 59);
        assertThrows(CoreException.class, () -> {
            new TimePeriod(startDate, endDate);
        });
    }

}