package com.hunko.missionmatching.core.domain;

import com.hunko.missionmatching.storage.MissionCursor;
import com.hunko.missionmatching.storage.MissionMapper;
import com.hunko.missionmatching.storage.MissionRepository;
import com.hunko.missionmatching.util.CursorUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Limit;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MissionReader {

    private final MissionRepository missionRepository;

    public List<Mission> readAllMission(MissionCursor missionCursor) {
        int count = missionCursor.limit();
        List<Mission> result = new ArrayList<>(count);
        if (missionCursor.isFirst() || MissionStatus.ONGOING.equals(missionCursor.status())) {
            List<Mission> missions = readOngoingMission(missionCursor);
            result.addAll(missions);
            count -= missions.size();
        }
        if (count > 0) {
            MissionCursor request = getMissionCursorRequest(MissionStatus.PENDING,
                    missionCursor, count);
            List<Mission> missions = readPendingMission(request);
            result.addAll(missions);
            count -= missions.size();
        }
        if (count > 0) {
            MissionCursor request = getMissionCursorRequest(MissionStatus.COMPLETED,
                    missionCursor, count);
            List<Mission> missions = readCompletedMission(request);
            result.addAll(missions);
        }
        return result;
    }

    private static MissionCursor getMissionCursorRequest(MissionStatus pending, MissionCursor missionCursor, int count) {
        if (!pending.equals(missionCursor.status())) {
            return MissionCursor.empty(count);
        }
        return missionCursor.updateLimit(count);
    }

    public List<Mission> readOngoingMission(MissionCursor missionCursor) {
        return CursorUtil.fetchByCursor(missionCursor::isFirst,
                () -> missionRepository.findByStatus(MissionStatus.ONGOING,
                        Sort.by(Direction.ASC, "endDate"), Limit.of(missionCursor.limit())),
                () -> missionRepository.findMissionByEndDate(
                        missionCursor.end(),
                        missionCursor.id(),
                        MissionStatus.ONGOING,
                        Limit.of(missionCursor.limit())
                )
        );
    }

    public List<Mission> readPendingMission(MissionCursor missionCursor) {
        return CursorUtil.fetchByCursor(missionCursor::isFirst,
                () -> missionRepository.findByStatus(MissionStatus.PENDING,
                        Sort.by(Direction.ASC, "startDate"), Limit.of(missionCursor.limit())),
                () -> missionRepository.findMissionByStartDate(
                        missionCursor.start(),
                        missionCursor.id(),
                        MissionStatus.PENDING,
                        missionCursor.limit())
        );
    }

    public List<Mission> readCompletedMission(MissionCursor missionCursor) {
        return CursorUtil.fetchByCursor(missionCursor::isFirst,
                () -> missionRepository.findByStatus(MissionStatus.COMPLETED,
                        Sort.by(Direction.ASC, "endDate"), Limit.of(missionCursor.limit())),
                () -> missionRepository.findMissionByEndDate(
                        missionCursor.end(),
                        missionCursor.id(),
                        MissionStatus.COMPLETED,
                        Limit.of(missionCursor.limit()))
        );
    }

    public Optional<Mission> readById(Long id) {
        return missionRepository.findById(id).map(MissionMapper::toMission);
    }
}
