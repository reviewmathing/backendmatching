package com.hunko.missionmatching.core.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class User {

    private final Long id;
    private final String name;
    private final int unreviewedCount;
    private final int allowedReportCount;
}
