package com.hunko.missionmatching.core.application.config;

import com.hunko.missionmatching.core.domain.MissionReader;
import com.hunko.missionmatching.core.domain.ReviewGithubUrlUpdateService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DomainServiceConfig {

    @Bean
    public ReviewGithubUrlUpdateService reviewGithubUrlUpdateService(MissionReader missionReader) {
        return new ReviewGithubUrlUpdateService(missionReader);
    }
}
