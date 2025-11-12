package com.hunko.missionmatching.core.domain;

import com.hunko.missionmatching.core.exception.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MissionCreator {

    private final FutureDateSpecification futureDateSpecification;

    public Mission createMission(String title, TimePeriod timePeriod, Creator creator) {
        if (!futureDateSpecification.isSatisfiedBy(timePeriod)) {
            ErrorType.INVALID_INPUT.throwException();
        }
        return new Mission(title, timePeriod, creator);
    }
}
