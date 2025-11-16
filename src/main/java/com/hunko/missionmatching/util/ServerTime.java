package com.hunko.missionmatching.util;

import java.time.LocalDateTime;
import java.time.ZoneId;

public class ServerTime {

    static LocalDateTimeFactory SERVER_TIME_FACTORY = new LocalDateTimeFactory();
    public static final ZoneId SERVER_TIME_ZONE = ZoneId.of("Asia/Seoul");

    private ServerTime() {
    }

    public static LocalDateTime now() {
        return SERVER_TIME_FACTORY.now(SERVER_TIME_ZONE);
    }
}
