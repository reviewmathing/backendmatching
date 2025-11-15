package com.hunko.missionmatching.storage;

import com.hunko.missionmatching.core.domain.ReviewStatus;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewAssignmentRevieweeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private ReviewAssignmentEntity reviewAssignmentEntity;
    private Long revieweeId;
    @Enumerated(EnumType.STRING)
    private ReviewStatus reviewStatus;

    public ReviewAssignmentRevieweeEntity(Long id, ReviewAssignmentEntity reviewAssignmentEntity, Long revieweeId,
                                          ReviewStatus reviewStatus) {
        this.id = id;
        this.reviewAssignmentEntity = reviewAssignmentEntity;
        this.revieweeId = revieweeId;
        this.reviewStatus = reviewStatus;
    }
}
