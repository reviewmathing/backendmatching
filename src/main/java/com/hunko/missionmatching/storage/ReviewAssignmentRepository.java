package com.hunko.missionmatching.storage;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewAssignmentRepository extends JpaRepository<ReviewAssignmentEntity, Long> {
    Page<ReviewAssignmentEntity> findAllByReviewerId(Long reviewerId, Pageable of);
}
