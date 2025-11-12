package com.hunko.missionmatching.util;

import java.time.LocalDateTime;

public class DateUtil {
    public static boolean equalOrBefore(LocalDateTime before, LocalDateTime after) {
        return before.isBefore(after) || before.isEqual(after);
    }

    public static boolean equalOrAfter(LocalDateTime after, LocalDateTime before) {
        return after.isAfter(before) || before.isEqual(after);
    }
}