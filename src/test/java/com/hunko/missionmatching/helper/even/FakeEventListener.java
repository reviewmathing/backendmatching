package com.hunko.missionmatching.helper.even;

import com.hunko.missionmatching.core.domain.EventListener;
import java.util.ArrayList;
import java.util.List;

public class FakeEventListener implements EventListener {

    private List<Object> events = new ArrayList<>();

    @Override
    public void publish(Object event) {
        this.events.add(event);
    }

    public <T> T getEvent(Class<T> eventType) {
        if (this.events.size() == 1) {
            return eventType.cast(this.events.getFirst());
        }
        throw new IllegalStateException();
    }

    public <T> List<T> getEvents(Class<T> eventType) {
        return events.stream().map(eventType::cast).toList();
    }

    public void clear(){
        this.events.clear();
    }
}
