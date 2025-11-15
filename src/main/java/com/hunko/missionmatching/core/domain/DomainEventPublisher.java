package com.hunko.missionmatching.core.domain;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class DomainEventPublisher {

    private static final List<EventListener> defaultEventListeners = new ArrayList<>();
    private final List<EventListener> eventListeners = new ArrayList<>();
    private static ThreadLocal<DomainEventPublisher> domainEventPublisher = ThreadLocal.withInitial(DomainEventPublisher::new);

    private DomainEventPublisher() {
        eventListeners.addAll(defaultEventListeners);
    }

    public static void addDefaultEventListener(EventListener eventListener) {
        defaultEventListeners.add(eventListener);
    }

    public static DomainEventPublisher instance() {
        return domainEventPublisher.get();
    }

    public void published(Object event) {
        for (EventListener eventListener : eventListeners) {
            eventListener.publish(event);
        }
    }

    public void addListener(EventListener eventListener) {
        eventListeners.add(eventListener);
    }
}
