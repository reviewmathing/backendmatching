package com.hunko.missionmatching.core.application.service;

import com.hunko.missionmatching.core.domain.MissionId;
import com.hunko.missionmatching.core.domain.ReviewRequest;
import com.hunko.missionmatching.storage.ReviewRequestEntity;
import com.hunko.missionmatching.storage.ReviewRequestEntityMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.Collections;
import java.util.List;

public class ReviewRequestBatchReader {

    private final EntityManager entityManager;
    private final int pageSize;
    private int page = 0;
    private int minContentSize;
    private long totalCount = -1;
    private final MissionId missionId;

    public ReviewRequestBatchReader(EntityManager entityManager, int pageSize,int minContentSize, MissionId missionId) {
        this.entityManager = entityManager;
        this.pageSize = pageSize;
        this.missionId = missionId;
        this.minContentSize = minContentSize;
    }

    public List<ReviewRequest> reviewRequests() {
        if (totalCount == -1) {
            TypedQuery<Long> query = entityManager.createQuery(
                    "select count(e) from ReviewRequestEntity e where e.reviewRequestType = 'REQUEST' and e.missionId = :missionId",
                    Long.class);
            query.setParameter("missionId", missionId.toLong());
            totalCount = query.getSingleResult();
        }
        if (totalCount < getOffset()) {
            return Collections.emptyList();
        }
        TypedQuery<ReviewRequestEntity> query = entityManager.createQuery(
                "select e from ReviewRequestEntity e where e.reviewRequestType = 'REQUEST' and e.missionId = :missionId",
                ReviewRequestEntity.class);
        query.setParameter("missionId", missionId.toLong());
        query.setFirstResult(getOffset());
        query.setMaxResults(getPageSize());
        if(getPageSize() != pageSize){
            page++;
        }
        page++;
        List<ReviewRequest> list = query.getResultList().stream().map(ReviewRequestEntityMapper::toReviewRequest)
                .toList();
        return list;
    }

    private int getOffset() {
        return page * pageSize;
    }

    private int getPageSize() {
        if (totalCount - (getOffset() + pageSize) < minContentSize) {
            return (int) (pageSize + (totalCount - (getOffset() + pageSize)));
        }
        return pageSize;
    }
}
