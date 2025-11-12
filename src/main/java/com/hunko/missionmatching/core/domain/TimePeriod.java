package com.hunko.missionmatching.core.domain;

import com.hunko.missionmatching.core.exception.ErrorType;
import java.time.LocalDateTime;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class TimePeriod {

    private final LocalDateTime startDate;
    private final LocalDateTime endDate;

    public TimePeriod(LocalDateTime startDate, LocalDateTime endDate) {
        if (startDate == null || endDate == null || startDate.isAfter(endDate)) {
            ErrorType.INVALID_INPUT.throwException();
        }
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
