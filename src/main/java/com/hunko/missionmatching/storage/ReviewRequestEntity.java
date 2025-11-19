package com.hunko.missionmatching.storage;

import com.hunko.missionmatching.core.domain.ReviewRequestType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@Table(
        indexes = {
                @Index(name = "mission_id_requestor_id_reviewRequest_type_idx", columnList = "missionId,requesterId,reviewRequestType", unique = true),
                @Index(name = "requester_id_and_id",columnList = "requesterId,id")
        }
)
public class ReviewRequestEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long missionId;
    private Long requesterId;
    private Integer reviewCount;
    private String githubUrl;
    @Enumerated(EnumType.STRING)
    private ReviewRequestType reviewRequestType;

    public ReviewRequestEntity(Long id, Long missionId, Long requesterId, Integer reviewCount, String githubUrl,
                               ReviewRequestType reviewRequestType) {
        this.id = id;
        this.missionId = missionId;
        this.requesterId = requesterId;
        this.reviewCount = reviewCount;
        this.githubUrl = githubUrl;
        this.reviewRequestType = reviewRequestType;
    }
}
