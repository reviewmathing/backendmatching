package com.hunko.missionmatching.storage;

import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ReviewAssignmentRevieweeRepository extends JpaRepository<ReviewAssignmentRevieweeEntity, Long> {
    List<ReviewAssignmentRevieweeEntity> findAllByReviewAssignmentEntityIn(List<ReviewAssignmentEntity> list);

    List<ReviewAssignmentRevieweeEntity> findByReviewAssignmentEntity(ReviewAssignmentEntity reviewAssignmentEntity);

    @Query("""
select r
from ReviewAssignmentRevieweeEntity r
join fetch r.reviewAssignmentEntity ra
where ra.missionId = :missionId
and r.revieweeId = :revieweeId
""")
    List<ReviewAssignmentRevieweeEntity> findByMissionIdAndRevieweeId(Long missionId,Long revieweeId);
}
