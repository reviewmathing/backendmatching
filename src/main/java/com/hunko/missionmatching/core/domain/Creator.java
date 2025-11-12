package com.hunko.missionmatching.core.domain;

import java.util.Objects;

public class Creator {

    private final Long id;

    private Creator(Long id) {
        this.id = id;
    }

    public static Creator of(long id) {
        return new Creator(id);
    }

    public Long toLong() {
        return this.id;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Creator creator = (Creator) o;
        return Objects.equals(id, creator.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
