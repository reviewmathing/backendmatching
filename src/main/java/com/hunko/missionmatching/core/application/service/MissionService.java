package com.hunko.missionmatching.core.application.service;

import com.hunko.missionmatching.core.Authorities;
import com.hunko.missionmatching.core.domain.Creator;
import com.hunko.missionmatching.core.domain.MissionCreator;
import com.hunko.missionmatching.core.domain.MissionReader;
import com.hunko.missionmatching.core.domain.MissionSaver;
import com.hunko.missionmatching.core.domain.DomainEventPublisher;
import com.hunko.missionmatching.core.exception.ErrorType;
import com.hunko.missionmatching.core.domain.Mission;
import com.hunko.missionmatching.storage.MissionCursor;
import com.hunko.missionmatching.core.domain.MissionRegistered;
import com.hunko.missionmatching.core.domain.TimePeriod;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MissionService {

    private final MissionCreator missionCreator;
    private final MissionSaver missionSaver;
    private final MissionReader missionReader;

    public List<Mission> loadFrom(Authorities authorities, MissionCursor pageRequest) {
        if(Authorities.ADMIN.equals(authorities)) {
            return missionReader.readAllMission(pageRequest);
        }
        return missionReader.readOngoingMission(pageRequest);
    }

    @Transactional
    public Long register(String title, TimePeriod timePeriod, Creator creator) {
        Mission mission = missionCreator.createMission(title, timePeriod, creator);
        Long id = missionSaver.save(mission);
        DomainEventPublisher.instance().published(new MissionRegistered(id));
        return id;
    }

    @Transactional
    public void updateOngoing(Long id, LocalDateTime startedTime) {
      Mission mission =  missionReader.readById(id).orElseThrow(ErrorType.ENTITY_NOT_FOUND::toException);
      mission.updateOngoing(startedTime);
      missionSaver.save(mission);
    }

    @Transactional
    public void updateCompleted(Long id, LocalDateTime endDate) {
        Mission mission =  missionReader.readById(id).orElseThrow(ErrorType.ENTITY_NOT_FOUND::toException);
        mission.updateCompleted(endDate);
        missionSaver.save(mission);
    }
}
