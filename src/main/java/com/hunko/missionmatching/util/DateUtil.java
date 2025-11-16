package com.hunko.missionmatching.util;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

public class DateUtil {

    public static boolean equalOrBefore(LocalDateTime before, LocalDateTime after) {
        return before.isBefore(after) || before.isEqual(after);
    }

    public static boolean equalOrAfter(LocalDateTime after, LocalDateTime before) {
        return after.isAfter(before) || before.isEqual(after);
    }

    public static LocalDateTime toServerDateTime(ZonedDateTime zonedDateTime) {
        return zonedDateTime.withZoneSameInstant(ServerTime.SERVER_TIME_ZONE).toLocalDateTime();
    }
}