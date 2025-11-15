package com.hunko.missionmatching.core.domain;

import java.util.Objects;
import lombok.Getter;

@Getter
public class Reviewee {

    private final Long id;
    private final RevieweeId revieweeId;
    private final GithubUri githubUri;
    private final ReviewStatus reviewStatus;

    public Reviewee(Long id, RevieweeId revieweeId, GithubUri githubUri, ReviewStatus reviewStatus) {
        this.id = id;
        this.revieweeId = revieweeId;
        this.githubUri = githubUri;
        this.reviewStatus = reviewStatus;
    }

    public Reviewee(RevieweeId revieweeId, GithubUri githubUri, ReviewStatus reviewStatus) {
        this(null, revieweeId, githubUri, reviewStatus);
    }

    public Reviewee(RevieweeId revieweeId, GithubUri githubUri) {
        this(revieweeId, githubUri, ReviewStatus.PENDING);
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
