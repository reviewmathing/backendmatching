package com.hunko.missionmatching.core.domain;

import com.hunko.missionmatching.core.exception.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserValidator {

    private final UserReader userReader;

    public boolean canRequestReview(Long userId) {
        return true;
    }
}
