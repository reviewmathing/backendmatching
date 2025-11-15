package com.hunko.missionmatching.storage;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRequestRepository extends JpaRepository<ReviewRequestEntity, Long> {
    Optional<ReviewRequestEntity> findByRequesterIdAndId(Long requesterId, Long id);

    List<ReviewRequestEntity> findAllByMissionId(Long missionId);
}
