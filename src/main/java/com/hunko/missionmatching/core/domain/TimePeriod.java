package com.hunko.missionmatching.core.domain;

import com.hunko.missionmatching.core.exception.ErrorType;
import com.hunko.missionmatching.util.DateUtil;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class TimePeriod {

    private final ZonedDateTime startDate;
    private final ZonedDateTime endDate;

    public TimePeriod(ZonedDateTime startDate, ZonedDateTime endDate) {
        if (startDate == null || endDate == null || startDate.isAfter(endDate)) {
            ErrorType.INVALID_INPUT.throwException();
        }
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public LocalDateTime getServerStartDateTime() {
        return DateUtil.toServerDateTime(startDate);
    }

    public LocalDateTime getServerEndDateTime() {
        return DateUtil.toServerDateTime(endDate);
    }

    public LocalDateTime getOriginStartDate() {
        return startDate.toLocalDateTime();
    }

    public LocalDateTime getOriginEndDate() {
        return endDate.toLocalDateTime();
    }

    public boolean isEqualsStartDate(LocalDateTime startDate) {
        LocalDateTime serverDateTime = DateUtil.toServerDateTime(this.startDate);
        return serverDateTime.equals(startDate);
    }

    public boolean isEqualsEndDate(LocalDateTime endDate) {
        LocalDateTime serverDateTime = DateUtil.toServerDateTime(this.endDate);
        return serverDateTime.equals(endDate);
    }

    public ZoneId getZoneId() {
        return endDate.getZone();
    }
}
