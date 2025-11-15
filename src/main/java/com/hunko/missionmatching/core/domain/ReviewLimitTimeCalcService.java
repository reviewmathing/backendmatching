package com.hunko.missionmatching.core.domain;

import java.time.LocalTime;
import java.time.ZonedDateTime;

public class ReviewLimitTimeCalcService {

    private static final Long AFTER_DAYS = 3L;

    public ZonedDateTime calc(ZonedDateTime dateTime) {
        ZonedDateTime zonedDateTime = dateTime.plusDays(AFTER_DAYS);
        zonedDateTime.toOffsetDateTime();
        return zonedDateTime.with(LocalTime.MAX);
    }
}
