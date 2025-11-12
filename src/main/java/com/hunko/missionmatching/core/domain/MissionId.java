package com.hunko.missionmatching.core.domain;

import java.util.Objects;

public class MissionId {
    private final Long id;

    private MissionId(Long id) {
        this.id = id;
    }

    public static MissionId of(Long id) {
        return new MissionId(id);
    }

    public static MissionId emtpy() {
        return new MissionId(null);
    }

    public Long toLong() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MissionId missionId = (MissionId) o;
        return Objects.equals(id, missionId.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
