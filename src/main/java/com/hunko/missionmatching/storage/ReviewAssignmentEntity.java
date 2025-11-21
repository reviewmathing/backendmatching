package com.hunko.missionmatching.storage;

import com.hunko.missionmatching.core.domain.ReviewAssignmentStatus;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import java.time.ZonedDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(indexes = {
        @Index(name = "revierwer_id_and_mission_id", columnList = "reviewerId,missionId"),
})
public class ReviewAssignmentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;
    @Getter
    private Long missionId;
    @Getter
    private Long reviewerId;
    @AttributeOverrides({
            @AttributeOverride(name = "localDateTime", column = @Column(name = "limit_time")),
            @AttributeOverride(name = "zoneId", column = @Column(name = "limit_time_time_zone_id"))
    })
    private ZoneDateTimeJpa limitTime;

    @Getter
    @Enumerated(EnumType.STRING)
    private ReviewAssignmentStatus reviewAssignmentStatus;

    public ReviewAssignmentEntity(Long id, Long missionId, Long reviewerId, ZonedDateTime limitTime,
                                  ReviewAssignmentStatus reviewAssignmentStatus) {
        this.id = id;
        this.missionId = missionId;
        this.reviewerId = reviewerId;
        this.limitTime = new ZoneDateTimeJpa(limitTime);
        this.reviewAssignmentStatus = reviewAssignmentStatus;
    }

    public void setStatus(ReviewAssignmentStatus reviewAssignmentStatus) {
        this.reviewAssignmentStatus = reviewAssignmentStatus;
    }

    public ZonedDateTime getLimitTime() {
        return limitTime.toZoneDateTime();
    }
}
