package com.hunko.missionmatching.storage;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@Table(
        indexes = {
                @Index(name = "mission_id_requestor_id_idx", columnList = "missionId,requesterId", unique = true)
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

    public ReviewRequestEntity(Long id, Long missionId, Long requesterId, Integer reviewCount, String githubUrl) {
        this.id = id;
        this.missionId = missionId;
        this.requesterId = requesterId;
        this.reviewCount = reviewCount;
        this.githubUrl = githubUrl;
    }
}
