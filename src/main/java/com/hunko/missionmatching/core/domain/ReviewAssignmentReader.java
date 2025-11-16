package com.hunko.missionmatching.core.domain;

import com.hunko.missionmatching.storage.ReviewAssignmentEntity;
import com.hunko.missionmatching.storage.ReviewAssignmentEntityMapper;
import com.hunko.missionmatching.storage.ReviewAssignmentRepository;
import com.hunko.missionmatching.storage.ReviewAssignmentRevieweeEntity;
import com.hunko.missionmatching.storage.ReviewAssignmentRevieweeEntityMapper;
import com.hunko.missionmatching.storage.ReviewAssignmentRevieweeRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReviewAssignmentReader {

    private final ReviewAssignmentRepository reviewAssigmentRepository;
    private final ReviewAssignmentRevieweeRepository reviewAssigmentRevieweeRepository;

    public Page<ReviewAssignment> loadFrom(Long userId, Integer page, int limit) {
        Sort sort = Sort.by(Direction.DESC, "id");
        Page<ReviewAssignmentEntity> reviewAssignmentEntities = reviewAssigmentRepository.findAllByReviewerId(userId, PageRequest.of(page,limit, sort));
        List<ReviewAssignmentRevieweeEntity> revieweeEntities= reviewAssigmentRevieweeRepository.findAllByReviewAssignmentEntityIn(reviewAssignmentEntities.toList());
        return reviewAssignmentEntities.map((ra)->{
            List<ReviewAssignmentRevieweeEntity> list = revieweeEntities.stream()
                    .filter(rw -> rw.getReviewAssignmentEntity().equals(ra)).toList();
            return ReviewAssignmentEntityMapper.toReviewAssignment(ra,list);
        });
    }

    public Optional<ReviewAssignment> loadFrom(Long id) {
        return reviewAssigmentRepository.findById(id).map(this::toReviewAssignment);
    }

    private ReviewAssignment toReviewAssignment(ReviewAssignmentEntity reviewAssignmentEntity) {
        List<ReviewAssignmentRevieweeEntity> revieweeEntities = reviewAssigmentRevieweeRepository.findByReviewAssignmentEntity(reviewAssignmentEntity);
        return ReviewAssignmentEntityMapper.toReviewAssignment(reviewAssignmentEntity, revieweeEntities);
    }

    public List<ReviewAssignment> loadAssignmentsFrom(RevieweeId revieweeId) {
        List<ReviewAssignmentRevieweeEntity> reviewee = reviewAssigmentRevieweeRepository.findByRevieweeId(revieweeId.toLong());
        return reviewee.stream().map(ReviewAssignmentEntityMapper::toReviewAssignment).toList();
    }
}
