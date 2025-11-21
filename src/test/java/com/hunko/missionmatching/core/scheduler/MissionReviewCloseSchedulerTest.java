package com.hunko.missionmatching.core.scheduler;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.hunko.missionmatching.core.domain.MissionStatus;
import com.hunko.missionmatching.core.domain.ReviewAssignmentStatus;
import com.hunko.missionmatching.core.domain.ReviewLimitTimeCalcService;
import com.hunko.missionmatching.core.domain.TestMissionFactory;
import com.hunko.missionmatching.core.domain.UserReader;
import com.hunko.missionmatching.helper.StubFailMissionWalFactory;
import com.hunko.missionmatching.storage.MissionRepository;
import com.hunko.missionmatching.storage.ReviewAssignmentEntity;
import com.hunko.missionmatching.storage.ReviewAssignmentRepository;
import com.hunko.missionmatching.util.ServerTime;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.convention.TestBean;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
@SpringBatchTest
class MissionReviewCloseSchedulerTest {

    @Autowired
    private MissionReviewCloseScheduler missionReviewCloseScheduler;

    @Autowired
    private ReviewAssignmentRepository reviewAssignmentRepository;

    @Autowired
    private EntityManager entityManager;

    @MockitoBean
    private ReviewLimitTimeCalcService  reviewLimitTimeCalcService;

    @TestBean
    private MissionSchedulerWalFactory missionWalFactory;

    static MissionSchedulerWalFactory missionWalFactory() {
        return new StubFailMissionWalFactory();
    }

    @MockitoBean
    private UserReader userReader;

    @BeforeEach
    void setUp() {
        reviewAssignmentRepository.deleteAll();
    }

    @Test
    void 리뷰기간만료(){
        for(int i = 0; i < 10; i++) {
            ReviewAssignmentEntity reviewAssignmentEntity = new ReviewAssignmentEntity(
                    null,
                    1L,
                    1L,
                    ZonedDateTime.now(),
                    ReviewAssignmentStatus.NOT_CLEARED
            );
            reviewAssignmentRepository.save(reviewAssignmentEntity);
        }
        when(reviewLimitTimeCalcService.calc(any())).thenReturn(ZonedDateTime.now().plusSeconds(3));

        missionReviewCloseScheduler.schedule(
                TestMissionFactory.createMission(1L, MissionStatus.COMPLETED,1L)
        );

        await()
                .atMost(30, TimeUnit.SECONDS)
                .untilAsserted(()->{
                    TypedQuery<ReviewAssignmentEntity> query = entityManager.createQuery(
                            "select r from ReviewAssignmentEntity r where r.reviewAssignmentStatus = 'NOT_CLEARED'",
                            ReviewAssignmentEntity.class);
                    List<ReviewAssignmentEntity> resultList = query.getResultList();
                    assertThat(resultList).isEmpty();
                });
    }
}