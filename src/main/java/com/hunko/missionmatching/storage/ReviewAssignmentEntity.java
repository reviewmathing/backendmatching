package com.hunko.missionmatching.storage;

import com.hunko.missionmatching.core.domain.ReviewAssignmentStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.ZonedDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.TimeZoneStorage;
import org.hibernate.annotations.TimeZoneStorageType;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewAssignmentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long missionId;
    private Long reviewerId;
    @TimeZoneStorage(value = TimeZoneStorageType.NATIVE)
    private ZonedDateTime limitTime;
    @Enumerated(EnumType.STRING)
    private ReviewAssignmentStatus reviewAssignmentStatus;

    public ReviewAssignmentEntity(Long id, Long missionId, Long reviewerId, ZonedDateTime limitTime, ReviewAssignmentStatus reviewAssignmentStatus) {
        this.id = id;
        this.missionId = missionId;
        this.reviewerId = reviewerId;
        this.limitTime = limitTime;
        this.reviewAssignmentStatus = reviewAssignmentStatus;
    }

    public void setStatus(ReviewAssignmentStatus reviewAssignmentStatus) {
        this.reviewAssignmentStatus = reviewAssignmentStatus;
    }
}
