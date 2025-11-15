package com.hunko.missionmatching.storage;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewAssignmentRevieweeRepository extends JpaRepository<ReviewAssignmentRevieweeEntity, Long> {
    List<ReviewAssignmentRevieweeEntity> findAllByReviewAssignmentEntityIn(List<ReviewAssignmentEntity> list);
}
