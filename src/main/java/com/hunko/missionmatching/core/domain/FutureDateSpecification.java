package com.hunko.missionmatching.core.domain;

public interface FutureDateSpecification {
    boolean isSatisfiedBy(TimePeriod timePeriod);
}
