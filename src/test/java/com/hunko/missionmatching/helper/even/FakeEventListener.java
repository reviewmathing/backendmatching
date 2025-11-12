package com.hunko.missionmatching.helper.even;

import com.hunko.missionmatching.core.domain.EventListener;

public class FakeEventListener implements EventListener {

    private Object event;

    @Override
    public void publish(Object event) {
        this.event = event;
    }

    public <T> T getEvent(Class<T> eventType) {
        return eventType.cast(event);
    }
}
