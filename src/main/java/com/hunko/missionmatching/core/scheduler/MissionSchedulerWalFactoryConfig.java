package com.hunko.missionmatching.core.scheduler;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MissionSchedulerWalFactoryConfig {

    private @Value("${scheduler.log.path:''}") String path;

    @Bean
    public MissionSchedulerWalFactory missionSchedulerWal() {
        return new MissionSchedulerWalFactory(path);
    }
}
