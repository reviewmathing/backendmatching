package com.hunko.missionmatching.core.application.service;

import com.hunko.missionmatching.core.domain.ReviewAssignment;
import com.hunko.missionmatching.core.domain.Reviewee;
import com.hunko.missionmatching.storage.ReviewAssignmentEntity;
import com.hunko.missionmatching.storage.ReviewAssignmentEntityMapper;
import com.hunko.missionmatching.storage.ReviewAssignmentRepository;
import com.hunko.missionmatching.storage.ReviewAssignmentRevieweeEntity;
import com.hunko.missionmatching.storage.ReviewAssignmentRevieweeEntityMapper;
import com.hunko.missionmatching.storage.ReviewAssignmentRevieweeRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class ReviewAssignmentSaver {

    private final ReviewAssignmentRepository reviewAssignmentRepository;
    private final ReviewAssignmentRevieweeRepository reviewAssignmentRevieweeRepository;

    @Transactional
    public void save(List<ReviewAssignment> reviewAssignments) {
        List<ReviewAssignmentEntity> reviewAssignmentEntities = new ArrayList<>();
        List<ReviewAssignmentRevieweeEntity> revieweeEntities = new ArrayList<>();
        for (ReviewAssignment reviewAssignment : reviewAssignments) {
            ReviewAssignmentEntity entity = ReviewAssignmentEntityMapper.toEntity(reviewAssignment);
            List<Reviewee> reviewee = reviewAssignment.getReviewees();
            List<ReviewAssignmentRevieweeEntity> revieweeEntities1 = ReviewAssignmentRevieweeEntityMapper.toEntities(entity, reviewee);

            reviewAssignmentEntities.add(entity);
            revieweeEntities.addAll(revieweeEntities1);
        }

        reviewAssignmentRepository.saveAll(reviewAssignmentEntities);
        reviewAssignmentRevieweeRepository.saveAll(revieweeEntities);
    }

    public void save(ReviewAssignment reviewAssignment) {
        ReviewAssignmentEntity entity = ReviewAssignmentEntityMapper.toEntity(reviewAssignment);
        reviewAssignmentRepository.save(entity);
        List<Reviewee> reviewee = reviewAssignment.getReviewees();
        List<ReviewAssignmentRevieweeEntity> revieweeEntities1 = ReviewAssignmentRevieweeEntityMapper.toEntities(entity, reviewee);
        reviewAssignmentRevieweeRepository.saveAll(revieweeEntities1);
    }
}
