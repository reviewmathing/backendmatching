package com.hunko.missionmatching.storage;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewAssignmentRepository extends JpaRepository<ReviewAssignmentEntity, Long> {
    Page<ReviewAssignmentEntity> findAllByReviewerId(Long reviewerId, Pageable of);

    Optional<ReviewAssignmentEntity> findByReviewerIdAndMissionId(Long reviewerId, Long missionId);
}
