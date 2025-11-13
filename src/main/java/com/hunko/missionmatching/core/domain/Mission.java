package com.hunko.missionmatching.core.domain;

import com.hunko.missionmatching.core.exception.ErrorType;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class Mission {

    private final MissionId id;
    private final String title;
    private final TimePeriod timePeriod;
    private final Creator creator;
    private final GithubUri missionUrl;
    private MissionStatus status;


    public Mission(String title, TimePeriod timePeriod, Creator creator, GithubUri missionUrl) {
        this(MissionId.emtpy(), title, timePeriod, creator, MissionStatus.PENDING,missionUrl);
    }

    public Mission(MissionId id, String title, TimePeriod timePeriod, Creator creator, MissionStatus status, GithubUri missionUrl) {
        if (title == null || title.isEmpty() || creator == null || timePeriod == null) {
            ErrorType.INVALID_INPUT.throwException();
        }
        this.id = id;
        this.title = title;
        this.timePeriod = timePeriod;
        this.creator = creator;
        this.status = status;
        this.missionUrl = missionUrl;
    }

    public void updateOngoing(LocalDateTime startedTime) {
        if (!MissionStatus.PENDING.equals(this.status) || !this.timePeriod.isEqualsStartDate(startedTime)) {
            //todo : 추후 업데이트 예정
            ErrorType.INVALID_INPUT.throwException();
        }
        this.status = MissionStatus.ONGOING;
        DomainEventPublisher.instance().published(new MissionOngoinged(this.id.toLong()));
    }

    public void updateCompleted(LocalDateTime endDate) {
        if (!MissionStatus.ONGOING.equals(this.status) || !this.timePeriod.isEqualsEndDate(endDate)) {
            //todo : 추후 업데이트 예정
            ErrorType.INVALID_INPUT.throwException();
        }
        this.status = MissionStatus.COMPLETED;
        DomainEventPublisher.instance().published(new MissionCompleted(this.id.toLong()));
    }
}
