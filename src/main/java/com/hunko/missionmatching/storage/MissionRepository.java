package com.hunko.missionmatching.storage;

import com.hunko.missionmatching.core.domain.MissionStatus;
import java.time.ZonedDateTime;
import java.util.List;
import org.springframework.data.domain.Limit;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MissionRepository extends JpaRepository<MissionEntity, Long> {
    @Query("""
            SELECT m
            FROM MissionEntity m
            WHERE m.status = :status
            """)
    List<MissionEntity> findByStatus(MissionStatus status, Sort sort, Limit limit);

    @Query("""
            SELECT m
            FROM MissionEntity m
            WHERE m.status = :status
            and m.startDate > :cursorStartDate 
            or (m.startDate = :cursorStartDate and m.id > :cursorId)
            order by m.startDate asc
            """)
    List<MissionEntity> findMissionByStartDate(ZonedDateTime cursorStartDate, long cursorId, MissionStatus status,
                                               int limit);

    @Query("""
                SELECT m
                FROM MissionEntity m
                WHERE m.status = :status
                and m.endDate > :cursorEndDate 
                or (m.endDate = :cursorEndDate and m.id > :cursorId)
                order by m.endDate
            """)
    List<MissionEntity> findMissionByEndDate(ZonedDateTime cursorEndDate, long cursorId, MissionStatus status,
                                             Limit limit);
}
