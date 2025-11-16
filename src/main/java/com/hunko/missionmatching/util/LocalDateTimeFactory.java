package com.hunko.missionmatching.util;

import java.time.LocalDateTime;
import java.time.ZoneId;

public class LocalDateTimeFactory {

    public LocalDateTime now(ZoneId zoneId) {
        return LocalDateTime.now(zoneId);
    }
}
