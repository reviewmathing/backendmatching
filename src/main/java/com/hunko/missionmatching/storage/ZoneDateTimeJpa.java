package com.hunko.missionmatching.storage;

import com.hunko.missionmatching.util.DateUtil;
import com.hunko.missionmatching.util.ServerTime;
import jakarta.persistence.Embeddable;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ZoneDateTimeJpa {

    private LocalDateTime localDateTime;
    private String zoneId;

    public ZoneDateTimeJpa(ZonedDateTime zonedDateTime) {
        localDateTime = DateUtil.toServerDateTime(zonedDateTime);
        zoneId = zonedDateTime.getZone().getId();
    }

    public ZonedDateTime toZoneDateTime() {
        ZonedDateTime zonedDateTime = localDateTime.atZone(ServerTime.SERVER_TIME_ZONE);
        return zonedDateTime.withZoneSameInstant(ZoneId.of(zoneId));
    }
}
