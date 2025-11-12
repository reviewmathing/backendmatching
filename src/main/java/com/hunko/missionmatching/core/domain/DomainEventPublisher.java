package com.hunko.missionmatching.core.domain;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class DomainEventPublisher {

    private final EventListener eventListener;
    private static DomainEventPublisher domainEventPublisher;

    public DomainEventPublisher(EventListener eventListener) {
        this.eventListener = eventListener;
    }

    @PostConstruct
    public void init() {
        this.domainEventPublisher = this;
    }

    public static DomainEventPublisher instance() {
        return domainEventPublisher;
    }

    public void published(Object event) {
        eventListener.publish(event);
    }
}
