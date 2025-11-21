package com.hunko.missionmatching.core.application.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.hunko.missionmatching.core.domain.MissionId;
import com.hunko.missionmatching.core.domain.ReviewRequest;
import com.hunko.missionmatching.core.domain.ReviewRequestType;
import com.hunko.missionmatching.core.domain.UserReader;
import com.hunko.missionmatching.core.scheduler.MissionSchedulerWalFactory;
import com.hunko.missionmatching.helper.StubFailMissionWalFactory;
import com.hunko.missionmatching.storage.ReviewRequestEntity;
import com.hunko.missionmatching.storage.ReviewRequestRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.convention.TestBean;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
class ReviewRequestBatchReaderTest {

    @Autowired
    private ReviewRequestRepository reviewRequestRepository;

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @TestBean
    private MissionSchedulerWalFactory missionWalFactory;

    static MissionSchedulerWalFactory missionWalFactory() {
        return new StubFailMissionWalFactory();
    }

    @MockitoBean
    private UserReader userReader;

    @Autowired
    private EntityManager em;

    @BeforeEach
    void setUp() {
        EntityManager entityManager1 = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager1.getTransaction();
        transaction.begin();
        entityManager1.createQuery("delete from ReviewRequestEntity").executeUpdate();
        transaction.commit();
    }

    @Test
    @Transactional
    void 최소컨텐츠사이즈보다_다음페이지가_많으면_정상반환() {
        for (int i = 0; i < 9; i++) {
            ReviewRequestEntity entity = new ReviewRequestEntity(null, 1L, (long) i, 3, "http://github.com",
                    ReviewRequestType.REQUEST);
            reviewRequestRepository.save(entity);
        }

        ReviewRequestBatchReader reviewRequestBatchReader = new ReviewRequestBatchReader(em, 5, 5,MissionId.of(1L));
        List<ReviewRequest> reviewRequests = reviewRequestBatchReader.reviewRequests();
        assertThat(reviewRequests).hasSize(9);
    }

    @Test
    @Transactional
    void 최소컨텐츠사이즈보다_다음페이지의_컨텐츠가_적으면_합쳐서반환() {
        for (int i = 0; i < 11; i++) {
            ReviewRequestEntity entity = new ReviewRequestEntity(null, 1L, (long) i, 3, "http://github.com",
                    ReviewRequestType.REQUEST);
            reviewRequestRepository.save(entity);
        }

        ReviewRequestBatchReader reviewRequestBatchReader = new ReviewRequestBatchReader(em, 5, 5,MissionId.of(1L));
        assertThat(reviewRequestBatchReader.reviewRequests()).hasSize(5);
        assertThat(reviewRequestBatchReader.reviewRequests()).hasSize(6);
    }
}