package com.hunko.missionmatching.core.presentaion;

import static org.assertj.core.api.Assertions.assertThat;

import com.hunko.missionmatching.core.domain.GithubUri;
import com.hunko.missionmatching.core.domain.MissionSaver;
import com.hunko.missionmatching.core.domain.MissionStatus;
import com.hunko.missionmatching.core.domain.ReviewRequestType;
import com.hunko.missionmatching.core.domain.TestMissionFactory;
import com.hunko.missionmatching.core.domain.UserReader;
import com.hunko.missionmatching.core.scheduler.MissionSchedulerWalFactory;
import com.hunko.missionmatching.helper.RequestBuildersHelper;
import com.hunko.missionmatching.helper.StubFailMissionWalFactory;
import com.hunko.missionmatching.storage.ReviewRequestEntity;
import com.hunko.missionmatching.storage.ReviewRequestRepository;
import com.jayway.jsonpath.JsonPath;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.bean.override.convention.TestBean;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ReviewRequestIntegrationTest {

    @Autowired
    private ReviewRequestRepository reviewRequestRepository;

    @Autowired
    private MissionSaver missionSaver;

    @Autowired
    private MockMvc mockMvc;

    @TestBean
    private MissionSchedulerWalFactory missionWalFactory;

    static MissionSchedulerWalFactory missionWalFactory() {
        return new StubFailMissionWalFactory();
    }

    @MockitoBean
    private UserReader userReader;

    @Test
    void 리뷰요청저장() throws Exception {
        Long missionId = missionSaver.save(TestMissionFactory.createMission(null, MissionStatus.ONGOING, 1L));
        MvcResult result = mockMvc.perform(
                RequestBuildersHelper.post("/api/missions/" + missionId)
                        .authentication("1", "USER")
                        .content(Map.of(
                                "reviewCount", 3L
                        ))
        ).andReturn();

        assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());
        String contentAsString = result.getResponse().getContentAsString();
        Integer requestId = JsonPath.read(contentAsString, "$.id");
        Optional<ReviewRequestEntity> entityOptional = reviewRequestRepository.findById((long) requestId);
        assertThat(entityOptional).isPresent();
        ReviewRequestEntity entity = entityOptional.get();
        assertThat(entity.getMissionId()).isEqualTo(missionId);
        assertThat(entity.getReviewCount()).isEqualTo(3);
        assertThat(entity.getRequesterId()).isEqualTo(1L);
    }

    @Test
    void 어브민리뷰요청() throws Exception {
        Long missionId = missionSaver.save(TestMissionFactory.createMission(null, MissionStatus.ONGOING, 1L));
        MvcResult result = mockMvc.perform(
                RequestBuildersHelper.post("/api/missions/" + missionId)
                        .authentication("1", "ADMIN")
                        .content(Map.of(
                                "reviewCount", 3L
                        ))
        ).andReturn();

        assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.FORBIDDEN.value());
    }

    @Test
    void 비인증리뷰요청() throws Exception {
        Long missionId = missionSaver.save(TestMissionFactory.createMission(null, MissionStatus.ONGOING, 1L));
        MvcResult result = mockMvc.perform(
                RequestBuildersHelper.post("/api/missions/" + missionId)
                        .nonAuthenticated()
                        .content(Map.of(
                                "reviewCount", 3L
                        ))
        ).andReturn();

        assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.FORBIDDEN.value());
    }

    @Test
    void 리뷰개수제한보다_많이_요청() throws Exception {
        Long missionId = missionSaver.save(TestMissionFactory.createMission(null, MissionStatus.ONGOING, 1L));
        MvcResult result = mockMvc.perform(
                RequestBuildersHelper.post("/api/missions/" + missionId)
                        .authentication("1", "USER")
                        .content(Map.of(
                                "reviewCount", 6
                        ))
        ).andReturn();

        assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void 리뷰개수제한보다_적게_요청() throws Exception {
        Long missionId = missionSaver.save(TestMissionFactory.createMission(null, MissionStatus.ONGOING, 1L));
        MvcResult result = mockMvc.perform(
                RequestBuildersHelper.post("/api/missions/" + missionId)
                        .authentication("1", "USER")
                        .content(Map.of(
                                "reviewCount", 0
                        ))
        ).andReturn();

        assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void 요청에_url업데이트() throws Exception {
        GithubUri githubUri = GithubUri.of("https://github.com");
        Long missionId = missionSaver.save(
                TestMissionFactory.createMission(null, MissionStatus.ONGOING, 1L, githubUri));
        ReviewRequestEntity reviewRequestEntity = reviewRequestRepository.save(new ReviewRequestEntity(
                null,
                missionId,
                1L,
                3,
                null,
                ReviewRequestType.REQUEST
        ));
        GithubUri requestUrl = GithubUri.of("https://github.com" + "/test");

        MvcResult result = mockMvc.perform(
                RequestBuildersHelper.post("/api/missions/" + missionId + "/reviews/" + reviewRequestEntity.getId())
                        .authentication("1", "USER")
                        .content(Map.of(
                                "uri", requestUrl.toUriString(),
                                "reviewCount",2
                        ))
        ).andReturn();

        assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());
        ReviewRequestEntity resultEntity = reviewRequestRepository.findById(reviewRequestEntity.getId()).get();
        assertThat(resultEntity.getGithubUrl()).isEqualTo(requestUrl.toUriString());
        assertThat(resultEntity.getReviewCount()).isEqualTo(2);
    }

    @Test
    void 어드민이_url업데이트() throws Exception {
        GithubUri githubUri = GithubUri.of("https://github.com");
        Long missionId = missionSaver.save(
                TestMissionFactory.createMission(null, MissionStatus.ONGOING, 1L, githubUri));
        ReviewRequestEntity reviewRequestEntity = reviewRequestRepository.save(new ReviewRequestEntity(
                null,
                missionId,
                1L,
                3,
                null,
                ReviewRequestType.REQUEST
        ));
        GithubUri requestUrl = GithubUri.of("https://github.com" + "/test");

        MvcResult result = mockMvc.perform(
                RequestBuildersHelper.post("/api/missions/" + missionId + "/reviews/" + reviewRequestEntity.getId())
                        .authentication("1", "ADMIN")
                        .content(Map.of(
                                "uri", requestUrl.toUriString()
                        ))
        ).andReturn();

        assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.FORBIDDEN.value());
    }

    @Test
    void 비인증회원이_url업데이트() throws Exception {
        GithubUri githubUri = GithubUri.of("https://github.com");
        Long missionId = missionSaver.save(
                TestMissionFactory.createMission(null, MissionStatus.ONGOING, 1L, githubUri));
        ReviewRequestEntity reviewRequestEntity = reviewRequestRepository.save(new ReviewRequestEntity(
                null,
                missionId,
                1L,
                3,
                null,
                ReviewRequestType.REQUEST
        ));
        GithubUri requestUrl = GithubUri.of("https://github.com" + "/test");

        MvcResult result = mockMvc.perform(
                RequestBuildersHelper.post("/api/missions/" + missionId + "/reviews/" + reviewRequestEntity.getId())
                        .nonAuthenticated()
                        .content(Map.of(
                                "uri", requestUrl.toUriString()
                        ))
        ).andReturn();

        assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.FORBIDDEN.value());
    }

    @Test
    void 리뷰신청_취소() throws Exception {
        Long missionId = missionSaver.save(TestMissionFactory.createMission(null, MissionStatus.ONGOING, 1L));
        ReviewRequestEntity reviewRequestEntity = reviewRequestRepository.save(new ReviewRequestEntity(
                null,
                missionId,
                1L,
                3,
                null,
                ReviewRequestType.REQUEST
        ));

        MvcResult result = mockMvc.perform(
                RequestBuildersHelper.delete("/api/missions/" + missionId + "/reviews/" + reviewRequestEntity.getId())
                        .authentication("1", "USER")
        ).andReturn();

        assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());
        ReviewRequestEntity resultEntity = reviewRequestRepository.findById(reviewRequestEntity.getId()).get();
        assertThat(resultEntity.getReviewRequestType()).isEqualTo(ReviewRequestType.CANCEL);
    }

    @Test
    void 어드민_리뷰신청_취소() throws Exception {
        Long missionId = missionSaver.save(TestMissionFactory.createMission(null, MissionStatus.ONGOING, 1L));
        ReviewRequestEntity reviewRequestEntity = reviewRequestRepository.save(new ReviewRequestEntity(
                null,
                missionId,
                1L,
                3,
                null,
                ReviewRequestType.REQUEST
        ));

        MvcResult result = mockMvc.perform(
                RequestBuildersHelper.delete("/api/missions/" + missionId + "/reviews/" + reviewRequestEntity.getId())
                        .authentication("1", "ADMIN")
        ).andReturn();

        assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.FORBIDDEN.value());
    }

    @Test
    void 비인증회원_리뷰신청_취소() throws Exception {
        Long missionId = missionSaver.save(TestMissionFactory.createMission(null, MissionStatus.ONGOING, 1L));
        ReviewRequestEntity reviewRequestEntity = reviewRequestRepository.save(new ReviewRequestEntity(
                null,
                missionId,
                1L,
                3,
                null,
                ReviewRequestType.REQUEST
        ));

        MvcResult result = mockMvc.perform(
                RequestBuildersHelper.delete("/api/missions/" + missionId + "/reviews/" + reviewRequestEntity.getId())
                        .nonAuthenticated()
        ).andReturn();

        assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.FORBIDDEN.value());
    }
}
