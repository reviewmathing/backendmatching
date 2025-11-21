package com.hunko.missionmatching.core.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

import com.hunko.missionmatching.core.domain.GithubUri;
import com.hunko.missionmatching.core.domain.Mission;
import com.hunko.missionmatching.core.domain.MissionId;
import com.hunko.missionmatching.core.domain.MissionStatus;
import com.hunko.missionmatching.core.domain.Requester;
import com.hunko.missionmatching.core.domain.ReviewRequest;
import com.hunko.missionmatching.core.domain.ReviewRequestId;
import com.hunko.missionmatching.core.domain.ReviewRequestType;
import com.hunko.missionmatching.core.domain.TestMissionFactory;
import com.hunko.missionmatching.core.domain.UserReader;
import com.hunko.missionmatching.core.scheduler.MissionSchedulerWalFactory;
import com.hunko.missionmatching.helper.StubFailMissionWalFactory;
import com.hunko.missionmatching.storage.MissionEntity;
import com.hunko.missionmatching.storage.MissionMapper;
import com.hunko.missionmatching.storage.ReviewRequestEntity;
import com.hunko.missionmatching.storage.ReviewRequestEntityMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.convention.TestBean;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
class MatchBatchServiceTestContainersTest {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Autowired
    private MatchBatchService matchBatchService;

    @TestBean
    private MissionSchedulerWalFactory missionWalFactory;

    static MissionSchedulerWalFactory missionWalFactory() {
        return new StubFailMissionWalFactory();
    }

    @MockitoBean
    private UserReader userReader;

    private Mission testMission;

    @BeforeEach
    void setUp() {
        EntityManager entityManager1 = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager1.getTransaction();
        transaction.begin();
        entityManager1.createQuery("delete from ReviewRequestEntity").executeUpdate();
        Mission mission = TestMissionFactory.createMission(null, MissionStatus.COMPLETED, 1L);
        MissionEntity missionEntity = MissionMapper.toMissionEntity(mission);
        entityManager1.persist(missionEntity);
        testMission = MissionMapper.toMission(missionEntity);
        transaction.commit();
    }

    @Test
    @DisplayName("실제 DB와 ExecutorService를 사용하여 100개의 ReviewRequest를 배치 처리한다")
    void shouldProcessWithRealDatabaseAndExecutor() {
        // given
        int requestCount = 10000;
        EntityManager entityManager1 = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager1.getTransaction();
        transaction.begin();
        createReviewRequests(entityManager1, testMission.getId(), requestCount);
        transaction.commit();

        matchBatchService.match(testMission);

        await()
                .atMost(10, TimeUnit.SECONDS)
                .untilAsserted(() -> {
                    var processed = findProcessedReviewRequests(testMission.getId());
                    assertThat(processed).hasSize(requestCount);
                });
    }

    public void createReviewRequests(EntityManager entityManager, MissionId missionId, int count) {
        for (int i = 0; i < count; i++) {
            ReviewRequest request = new ReviewRequest(
                    ReviewRequestId.empty(),
                    Requester.of((long) i),
                    missionId,
                    3,
                    GithubUri.of("http://github.com"),
                    ReviewRequestType.REQUEST
            );
            entityManager.persist(ReviewRequestEntityMapper.toEntity(request));
        }
        entityManager.flush();
    }

    private List<ReviewRequest> findProcessedReviewRequests(MissionId missionId) {
        return entityManager.createQuery(
                        "SELECT r FROM ReviewRequestEntity r WHERE r.missionId = :missionId and r.reviewRequestType = 'MATCHED'",
                        ReviewRequestEntity.class)
                .setParameter("missionId", missionId.toLong())
                .getResultList().stream().map(ReviewRequestEntityMapper::toReviewRequest).toList();
    }
}