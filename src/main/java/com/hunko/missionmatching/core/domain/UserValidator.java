package com.hunko.missionmatching.core.domain;

import com.hunko.missionmatching.core.exception.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserValidator {

    private final UserReader userReader;

    public boolean canRequestReview(Long userId) {
        User user = userReader.loadFrom(userId).orElseThrow(ErrorType.ENTITY_NOT_FOUND::toException);
        if (user.getAllowedReportCount() + user.getUnreviewedCount() >= 3) {
            return false;
        }
        return true;
    }
}
