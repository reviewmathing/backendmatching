package com.hunko.missionmatching.core.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import org.junit.jupiter.api.Test;

class ReviewLimitTimeCalcServiceTest {

    @Test
    void 리뷰마감시간계산() {
        LocalDateTime localDateTime = LocalDateTime.of(2024, 10, 18, 3, 0, 0);
        ZonedDateTime endDateTime = ZonedDateTime.of(localDateTime, ZoneId.of("America/New_York"));
        ReviewLimitTimeCalcService reviewLimitTimeCalcService = new ReviewLimitTimeCalcService();

        ZonedDateTime reviewLimitDateTime = reviewLimitTimeCalcService.calc(endDateTime);

        assertThat(reviewLimitDateTime).
                isEqualTo(
                        ZonedDateTime.of(
                                LocalDateTime.of(2024,10,21,23,59,59,999999999),
                                ZoneId.of("America/New_York")
                        )
                );

    }
}