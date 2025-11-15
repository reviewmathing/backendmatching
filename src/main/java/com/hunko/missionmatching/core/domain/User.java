package com.hunko.missionmatching.core.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class User {

    private final Long userId;
    private final String name;
}
