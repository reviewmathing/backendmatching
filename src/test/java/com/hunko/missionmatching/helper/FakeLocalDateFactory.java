package com.hunko.missionmatching.helper;

import com.hunko.missionmatching.util.LocalDateTimeFactory;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class FakeLocalDateFactory extends LocalDateTimeFactory {
    public FakeLocalDateFactory() {
    }

    @Override
    public LocalDateTime now(ZoneId zoneId) {
        return ZonedDateTime.of(LocalDateTime.of(2024,12,12,12,12), zoneId).toLocalDateTime();
    }
}
