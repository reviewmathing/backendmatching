package com.hunko.missionmatching.storage;

import com.hunko.missionmatching.core.domain.MissionStatus;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.validation.OverridesAttribute;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.TimeZoneColumn;
import org.hibernate.annotations.TimeZoneStorage;
import org.hibernate.annotations.TimeZoneStorageType;

@Entity
@Table(indexes = {
        @Index(name = "idx_status_end_date_id", columnList = "status,endDate,id"),
        @Index(name = "idx_status_start_date_id", columnList = "status,startDate,id")
})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MissionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;
    @Getter
    private String title;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "localDateTime", column = @Column(name = "start_date")),
            @AttributeOverride(name = "zoneId", column = @Column(name = "start_date_time_zone_id"))
    })
    private ZoneDateTimeJpa startDate;

    @AttributeOverrides({
            @AttributeOverride(name = "localDateTime", column = @Column(name = "end_date")),
            @AttributeOverride(name = "zoneId", column = @Column(name = "end_date_time_zone_id"))
    })
    private ZoneDateTimeJpa endDate;
    @Getter
    private Long creator;
    @Getter
    @Enumerated(EnumType.STRING)
    private MissionStatus status;
    @Getter
    private String missionUri;

    @Builder
    public MissionEntity(Long id, String title, ZonedDateTime startDate, ZonedDateTime endDate, Long creator,
                         MissionStatus status, String missionUri) {
        this.id = id;
        this.title = title;
        this.startDate = new ZoneDateTimeJpa(startDate);
        this.endDate = new  ZoneDateTimeJpa(endDate);
        this.creator = creator;
        this.status = status;
        this.missionUri = missionUri;
    }

    public ZonedDateTime getStartDate() {
        return startDate.toZoneDateTime();
    }

    public ZonedDateTime getEndDate() {
        return endDate.toZoneDateTime();
    }
}
