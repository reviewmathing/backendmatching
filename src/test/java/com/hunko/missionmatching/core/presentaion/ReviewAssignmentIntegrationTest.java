package com.hunko.missionmatching.core.presentaion;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hunko.missionmatching.core.application.service.ReviewAssignmentSaver;
import com.hunko.missionmatching.core.domain.Creator;
import com.hunko.missionmatching.core.domain.GithubUri;
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
import com.hunko.missionmatching.core.presentation.dto.ReviewAssigmentDto;
import com.hunko.missionmatching.core.presentation.dto.ReviewAssigmentDto.ListView;
import com.hunko.missionmatching.helper.RequestBuildersHelper;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
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


    @Test
    void 리뷰평가그룹조회() throws Exception {
        List<Mission> missions = initMission(20);
        initReviewAssigment(missions.stream().map(Mission::getId).toList());
        MvcResult result = mockMvc.perform(
                RequestBuildersHelper.get("/api/reviewassigment").
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
                            GithubUri.of("http://github.com")
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
                                    RevieweeId.of(2L)
                            )
                    )
            );
            assignments.add(reviewAssignment);
        }
        assignmentSaver.save(assignments);
    }
}