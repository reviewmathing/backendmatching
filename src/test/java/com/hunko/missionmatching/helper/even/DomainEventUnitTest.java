package com.hunko.missionmatching.helper.even;

import com.hunko.missionmatching.core.domain.DomainEventPublisher;
import org.junit.jupiter.api.BeforeEach;

public abstract class DomainEventUnitTest {

    private FakeEventListener eventListener;

    @BeforeEach
    public void setUp() {
        this.eventListener = new FakeEventListener();
        DomainEventPublisher domainEventPublisher = new DomainEventPublisher(this.eventListener);
        domainEventPublisher.init();
    }

    protected <T> T getEvent(Class<T> eventClass) {
        return eventListener.getEvent(eventClass);
    }
}
