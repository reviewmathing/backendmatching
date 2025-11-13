package com.hunko.missionmatching.core.domain;

import com.hunko.missionmatching.util.DateUtil;
import java.time.LocalDateTime;
import org.springframework.stereotype.Component;

@Component
public class FutureBasedOnNowSpecification implements FutureDateSpecification {

    @Override
    public boolean isSatisfiedBy(TimePeriod timePeriod) {
        LocalDateTime now = LocalDateTime.now().minusMinutes(1);
        return DateUtil.equalOrAfter(timePeriod.getServerStartDateTime(), now) && DateUtil.equalOrAfter(
                timePeriod.getServerEndDateTime(), now);
    }
}
