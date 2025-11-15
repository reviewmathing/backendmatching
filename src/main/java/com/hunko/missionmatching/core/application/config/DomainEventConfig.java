package com.hunko.missionmatching.core.application.config;

import com.hunko.missionmatching.core.domain.DomainEventPublisher;
import com.hunko.missionmatching.core.domain.EventListener;
import jakarta.annotation.PostConstruct;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class DomainEventConfig {

    private final List<EventListener> listeners;

    @PostConstruct
    public void init() {
        for (EventListener listener : listeners) {
            DomainEventPublisher.addDefaultEventListener(listener);
        }
    }
}
