package com.hunko.missionmatching.core.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

@SpringBootTest
class ReviewRequestSaverTest {

    @Autowired
    private ReviewRequestSaver reviewRequestSaver;

    @Test
    void 같은_미션에_두번저장시_예외() throws Exception {
        reviewRequestSaver.save(new ReviewRequest(new Requester(1L), MissionId.of(1L), 5));

        assertThatThrownBy(
                () -> reviewRequestSaver.save(new ReviewRequest(new Requester(1L), MissionId.of(1L), 5))).isInstanceOf(
                DataIntegrityViolationException.class);
    }
}