package com.hunko.missionmatching.core.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.hunko.missionmatching.core.scheduler.MissionSchedulerWalFactory;
import com.hunko.missionmatching.helper.StubFailMissionWalFactory;
import com.hunko.missionmatching.storage.ReviewRequestRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.bean.override.convention.TestBean;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
class ReviewRequestSaverTest {

    @Autowired
    private ReviewRequestSaver reviewRequestSaver;

    @Autowired
    private ReviewRequestRepository reviewRequestRepository;

    @TestBean
    private MissionSchedulerWalFactory missionWalFactory;

    static MissionSchedulerWalFactory missionWalFactory() {
        return new StubFailMissionWalFactory();
    }

    @MockitoBean
    private UserReader userReader;

    @BeforeEach
    void setUp() {
        reviewRequestRepository.deleteAll();
    }

    @Test
    void 같은_미션에_두번저장시_예외(){
        reviewRequestSaver.save(new ReviewRequest(new Requester(1L), MissionId.of(1L), 5));

        assertThatThrownBy(
                () -> reviewRequestSaver.save(new ReviewRequest(new Requester(1L), MissionId.of(1L), 5))).isInstanceOf(
                DataIntegrityViolationException.class);
    }
}