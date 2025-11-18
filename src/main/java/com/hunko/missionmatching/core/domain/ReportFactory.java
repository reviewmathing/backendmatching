package com.hunko.missionmatching.core.domain;

import com.hunko.missionmatching.core.exception.ErrorType;
import java.util.List;

public class ReportFactory {
    private ReportFactory() {
    }

    public static Report create(List<User> users, MissionId missionId, Long targetUserId, Long reportUserId,
                                GithubUri githubUri, String explanation) {
        Reporter reporter = toReporter(users, reportUserId);
        TargetUser targetUser = toTargetUser(users, targetUserId);
        return new Report(
                reporter,
                missionId,
                targetUser,
                githubUri,
                explanation
        );
    }

    private static Reporter toReporter(List<User> users, Long userId) {
        User user = users.stream().filter(u -> u.getId().equals(userId)).findFirst()
                .orElseThrow(ErrorType.ENTITY_NOT_FOUND::toException);
        return new Reporter(
                user.getId(),
                user.getName()
        );
    }

    private static TargetUser toTargetUser(List<User> users, Long userId) {
        User user = users.stream().filter(u -> u.getId().equals(userId)).findFirst()
                .orElseThrow(ErrorType.ENTITY_NOT_FOUND::toException);
        return new TargetUser(
                user.getId(),
                user.getName()
        );
    }
}
