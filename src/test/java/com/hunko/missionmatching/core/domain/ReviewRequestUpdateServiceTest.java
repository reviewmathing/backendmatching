package com.hunko.missionmatching.core.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.hunko.missionmatching.core.exception.CoreException;
import com.hunko.missionmatching.helper.FakeMissionReader;
import org.junit.jupiter.api.Test;

class ReviewRequestUpdateServiceTest {

    @Test
    void 깃허브_URL_업데이트() {
        FakeMissionReader fakeMissionReader = new FakeMissionReader();
        Mission mission = TestMissionFactory.createMission(1L, MissionStatus.ONGOING, 1L,
                TestGithubUrlFactory.createGithubUri());
        fakeMissionReader.addMission(mission);
        ReviewRequestUpdateService service = new ReviewRequestUpdateService(fakeMissionReader);
        GithubUri githubUri = TestGithubUrlFactory.createGithubUri("/test");
        ReviewRequest request = new ReviewRequest(ReviewRequestId.of(1L), Requester.of(1L), MissionId.of(1L), 5, null,
                ReviewRequestType.REQUEST);

        ReviewRequest result = service.updateGithubUrl(request, 2, githubUri);

        assertThat(result.getGithubUri()).isEqualTo(githubUri);
        assertThat(result.getReviewCount()).isEqualTo(2);
    }

    @Test
    void 요청URI가_미션의_하위_URI가_아님() {
        FakeMissionReader fakeMissionReader = new FakeMissionReader();
        Mission mission = TestMissionFactory.createMission(1L, MissionStatus.PENDING, 1L,
                TestGithubUrlFactory.createGithubUri("/test"));
        fakeMissionReader.addMission(mission);
        ReviewRequestUpdateService service = new ReviewRequestUpdateService(fakeMissionReader);
        GithubUri githubUri = TestGithubUrlFactory.createGithubUri("/tests/1");
        ReviewRequest request = new ReviewRequest(ReviewRequestId.of(1L), Requester.of(1L), MissionId.of(1L), 5, null,
                ReviewRequestType.REQUEST);

        assertThatThrownBy(() -> service.updateGithubUrl(request, 2, githubUri)).isInstanceOf(CoreException.class);
    }

    @Test
    void 요청한_미션이_존재하지_않음() {
        FakeMissionReader fakeMissionReader = new FakeMissionReader();
        ReviewRequestUpdateService service = new ReviewRequestUpdateService(fakeMissionReader);
        GithubUri githubUri = TestGithubUrlFactory.createGithubUri("/test");
        ReviewRequest request = new ReviewRequest(ReviewRequestId.of(1L), Requester.of(1L), MissionId.of(1L), 5, null,
                ReviewRequestType.REQUEST);

        assertThatThrownBy(() -> service.updateGithubUrl(request, 2, githubUri)).isInstanceOf(CoreException.class);
    }
}