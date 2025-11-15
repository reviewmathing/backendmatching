package com.hunko.missionmatching.core.domain;

import java.util.Objects;
import lombok.Getter;

@Getter
public class Reviewee {

    private final Long id;
    private final RevieweeId revieweeId;
    private final ReviewStatus reviewStatus;

    public Reviewee(Long id, RevieweeId revieweeId, ReviewStatus reviewStatus) {
        this.id = id;
        this.revieweeId = revieweeId;
        this.reviewStatus = reviewStatus;
    }

    public Reviewee(RevieweeId revieweeId, ReviewStatus reviewStatus) {
        this(null, revieweeId, reviewStatus);
    }

    public Reviewee(RevieweeId revieweeId) {
        this(revieweeId, ReviewStatus.PENDING);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Reviewee reviewee = (Reviewee) o;
        return Objects.equals(revieweeId, reviewee.revieweeId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(revieweeId);
    }
}
