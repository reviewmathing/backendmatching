package com.hunko.missionmatching.storage;

import com.hunko.missionmatching.core.domain.MissionStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(indexes = {
        @Index(name = "idx_status_end_date_id",columnList = "status,endDate,id"),
        @Index(name = "idx_status_start_date_id",columnList = "status,startDate,id")
})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class MissionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private ZonedDateTime startDate;
    private ZonedDateTime endDate;
    private Long creator;
    @Enumerated(EnumType.STRING)
    private MissionStatus status;

    @Builder
    public MissionEntity(Long id, String title, ZonedDateTime startDate, ZonedDateTime endDate, Long creator, MissionStatus status) {
        this.id = id;
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.creator = creator;
        this.status = status;
    }
}
