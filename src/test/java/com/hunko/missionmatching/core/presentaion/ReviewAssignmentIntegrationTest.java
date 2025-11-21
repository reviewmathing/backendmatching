package com.hunko.missionmatching.core.presentaion;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.hunko.missionmatching.core.application.service.ReviewAssignmentSaver;
import com.hunko.missionmatching.core.domain.Creator;
import com.hunko.missionmatching.core.domain.Mission;
import com.hunko.missionmatching.core.domain.MissionId;
import com.hunko.missionmatching.core.domain.MissionReader;
import com.hunko.missionmatching.core.domain.MissionSaver;
import com.hunko.missionmatching.core.domain.ReviewAssignment;
import com.hunko.missionmatching.core.domain.ReviewAssignmentReader;
import com.hunko.missionmatching.core.domain.Reviewee;
import com.hunko.missionmatching.core.domain.RevieweeId;
import com.hunko.missionmatching.core.domain.ReviewerId;
import com.hunko.missionmatching.core.domain.TimePeriod;
import com.hunko.missionmatching.core.domain.User;
import com.hunko.missionmatching.core.domain.UserReader;
import com.hunko.missionmatching.core.presentation.dto.ReviewAssigmentDto;
import com.hunko.missionmatching.core.presentation.dto.ReviewAssigmentDto.Details;
import com.hunko.missionmatching.core.presentation.dto.ReviewAssigmentDto.RevieweeDetails;
import com.hunko.missionmatching.core.scheduler.MissionSchedulerWalFactory;
import com.hunko.missionmatching.helper.RequestBuildersHelper;
import com.hunko.missionmatching.helper.StubFailMissionWalFactory;
import com.hunko.missionmatching.helper.TestGithubUri;
import com.hunko.missionmatching.storage.MissionRepository;
import com.hunko.missionmatching.storage.ReviewAssignmentRepository;
import com.hunko.missionmatching.storage.ReviewAssignmentRevieweeRepository;
import com.hunko.missionmatching.storage.ReviewRequestRepository;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.bean.override.convention.TestBean;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest
@AutoConfigureMockMvc
class ReviewAssignmentIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ReviewAssignmentSaver assignmentSaver;

    @Autowired
    private MissionSaver missionSaver;

    @Autowired
    private MissionReader missionReader;

    @Autowired
    private ReviewAssignmentReader reviewAssignmentReader;

    @Autowired
    private MissionRepository missionRepository;

    @Autowired
    private ReviewRequestRepository reviewRequestRepository;

    @Autowired
    private ReviewAssignmentRepository reviewAssignmentRepository;

    @Autowired
    private ReviewAssignmentRevieweeRepository reviewAssignmentRevieweeRepository;

    @TestBean
    private MissionSchedulerWalFactory missionWalFactory;

    static MissionSchedulerWalFactory missionWalFactory() {
        return new StubFailMissionWalFactory();
    }

    @MockitoBean
    private UserReader userReader;

    @BeforeEach
    void setUp() {
        reviewAssignmentRevieweeRepository.deleteAll();
        reviewAssignmentRepository.deleteAll();
        reviewRequestRepository.deleteAll();
        missionRepository.deleteAll();
    }

    @Test
    void 리뷰평가그룹조회() throws Exception {
        List<Mission> missions = initMission(20);
        initReviewAssigment(missions.stream().map(Mission::getId).toList());
        MvcResult result = mockMvc.perform(
                RequestBuildersHelper.get("/api/reviewassigments").
                        authentication("1", "USER")
        ).andReturn();

        assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());
        ObjectMapper objectMapper = new ObjectMapper();
        assertThat(result.getResponse().getContentAsString()).isEqualTo(
                objectMapper.writeValueAsString(
                        new ReviewAssigmentDto.ListView(
                                reviewAssignmentReader.loadFrom(1L, 0, 20), missions
                        )
                )
        );
    }

    @Test
    void 리뷰평가단건() throws Exception {
        String missionName = "test";
        Long missionId = missionSaver.save(
                new Mission(
                        missionName,
                        new TimePeriod(
                                ZonedDateTime.now(),
                                ZonedDateTime.now().plusDays(1)
                        ),
                        Creator.of(1L),
                        TestGithubUri.GITHUB_URI
                )
        );
        long userId = 1L;
        ReviewAssignment reviewAssignment = new ReviewAssignment(
                MissionId.of(missionId),
                ReviewerId.of(userId),
                ZonedDateTime.now(),
                List.of(
                        new Reviewee(
                                RevieweeId.of(2L),
                                TestGithubUri.GITHUB_URI
                        )
                )
        );
        assignmentSaver.save(List.of(reviewAssignment));
        String userName = "testUser";
        when(userReader.loadFrom(anyList())).thenReturn(List.of(
                new User(userId, userName),
                new User(2L, userName + "2")
        ));

        MvcResult result = mockMvc.perform(
                RequestBuildersHelper.get("/api/reviewassigments/1").
                        authentication(String.valueOf(userId), "USER")
        ).andReturn();

        assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());
        System.out.println(result.getResponse().getContentAsString());
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
        Details details = objectMapper.readValue(result.getResponse().getContentAsString(), Details.class);
        assertThat(details).isEqualTo(
                new ReviewAssigmentDto.Details(
                        missionId,
                        missionName,
                        1L,
                        reviewAssignment.getReviewAssignmentStatus(),
                        reviewAssignment.getLimitTime(),
                        List.of(),
                        List.of(
                                new RevieweeDetails(
                                        1L,
                                        userName + "2",
                                        reviewAssignment.getReviewees().getFirst().getGithubUri().toUriString(),
                                        reviewAssignment.getReviewees().getFirst().getReviewStatus()
                                )
                        )
                )
        );
    }

    private List<Mission> initMission(int missionCount) {
        ArrayList<Mission> result = new ArrayList<>();
        for (int i = 0; i < missionCount; i++) {
            Long id = missionSaver.save(
                    new Mission(
                            "test" + i,
                            new TimePeriod(
                                    ZonedDateTime.now(),
                                    ZonedDateTime.now().plusDays(1)
                            ),
                            Creator.of(1L),
                            TestGithubUri.GITHUB_URI
                    )
            );
            result.add(missionReader.readById(id).get());
        }
        return result;
    }

    private void initReviewAssigment(List<MissionId> missionIds) {
        ArrayList<ReviewAssignment> assignments = new ArrayList<>();
        for (MissionId missionId : missionIds) {
            ReviewAssignment reviewAssignment = new ReviewAssignment(
                    missionId,
                    ReviewerId.of(1L),
                    ZonedDateTime.now(),
                    List.of(
                            new Reviewee(
                                    RevieweeId.of(2L),
                                    TestGithubUri.GITHUB_URI
                            )
                    )
            );
            assignments.add(reviewAssignment);
        }
        assignmentSaver.save(assignments);
    }
}