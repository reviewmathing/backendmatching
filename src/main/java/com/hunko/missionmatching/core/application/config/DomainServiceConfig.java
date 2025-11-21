package com.hunko.missionmatching.core.application.config;

import com.hunko.missionmatching.core.domain.MissionReader;
import com.hunko.missionmatching.core.domain.ReviewRequestUpdateService;
import com.hunko.missionmatching.core.domain.ReviewLimitTimeCalcService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DomainServiceConfig {

    @Bean
    public ReviewRequestUpdateService reviewGithubUrlUpdateService(MissionReader missionReader) {
        return new ReviewRequestUpdateService(missionReader);
    }

    @Bean
    public ReviewLimitTimeCalcService reviewLimitTimeCalcService() {
        return new ReviewLimitTimeCalcService();
    }
}
