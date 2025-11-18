package com.hunko.missionmatching.storage;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewFailureRepository extends JpaRepository<ReviewFailureEntity, Integer> {
    List<ReviewFailureEntity> findAllByReviewerId(Long reviewerId);
}
