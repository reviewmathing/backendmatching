package com.hunko.missionmatching.storage;

import com.hunko.missionmatching.core.domain.MissionStatus;
import java.time.LocalDateTime;
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
            and (m.startDate.localDateTime > :cursorStartDate 
            or (m.startDate.localDateTime = :cursorStartDate and m.id > :cursorId))
            order by m.startDate.localDateTime asc
            """)
    List<MissionEntity> findMissionByStartDate(LocalDateTime cursorStartDate, long cursorId, MissionStatus status,
                                               int limit);

    @Query("""
                SELECT m
                FROM MissionEntity m
                WHERE m.status = :status
                and (m.endDate.localDateTime > :cursorEndDate 
                or (m.endDate.localDateTime = :cursorEndDate and m.id > :cursorId))
                order by m.endDate.localDateTime
            """)
    List<MissionEntity> findMissionByEndDate(LocalDateTime cursorEndDate, long cursorId, MissionStatus status,
                                             Limit limit);

    List<MissionEntity> findByStatus(MissionStatus missionStatus);
}
