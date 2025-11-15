package com.hunko.missionmatching.helper.even;

import com.hunko.missionmatching.core.domain.DomainEventPublisher;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

public abstract class DomainEventUnitTest {

    private FakeEventListener eventListener;

    @BeforeEach
    public void setUp() {
        this.eventListener = new FakeEventListener();
        DomainEventPublisher instance = DomainEventPublisher.instance();
        instance.addListener(eventListener);
    }

    protected <T> T getEvent(Class<T> eventClass) {
        return eventListener.getEvent(eventClass);
    }

    protected <T> List<T> getEvents(Class<T> eventClass) {
        return eventListener.getEvents(eventClass);
    }

    @AfterEach
    public void tearDown() {
        eventListener.clear();
    }
}
