package com.hunko.missionmatching.core;

import static org.awaitility.Awaitility.await;

import com.hunko.missionmatching.core.domain.MissionStatus;
import com.hunko.missionmatching.core.domain.UserReader;
import com.hunko.missionmatching.core.presentation.dto.MissionRequestDto;
import com.hunko.missionmatching.core.scheduler.MissionSchedulerWalFactory;
import com.hunko.missionmatching.helper.StubFailMissionWalFactory;
import com.hunko.missionmatching.storage.MissionEntity;
import com.hunko.missionmatching.storage.MissionRepository;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.convention.TestBean;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.web.client.RestTemplate;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MissionStatusIntegrationTest {

    @LocalServerPort
    private int port;

    private final RestTemplate restTemplate = new RestTemplate();

    @Autowired
    private MissionRepository missionRepository;

    @TestBean
    private MissionSchedulerWalFactory missionWalFactory;

    static MissionSchedulerWalFactory missionWalFactory() {
        return new StubFailMissionWalFactory();
    }

    @MockitoBean
    private UserReader userReader;

    @Test
    void 전체상태_플로우_검증() {
        String title = "test1";
        LocalDateTime start = LocalDateTime.now().plusSeconds(30);
        LocalDateTime end = LocalDateTime.now().plusMinutes(1);
        long creator = 1L;

        String url = "http://localhost:" + port + "/api/missions";
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-User-ID", String.valueOf(creator));
        headers.set("X-User-ROLE", "ADMIN");
        headers.setContentType(MediaType.APPLICATION_JSON);
        String missionUri = "https://github.com/woowacourse-precourse/java-lotto-8";
        MissionRequestDto missionRequestDto = new MissionRequestDto(title, start, end, ZoneId.systemDefault(),
                missionUri);
        // HttpEntity 생성 (헤더 + 바디)
        HttpEntity<MissionRequestDto> entity = new HttpEntity<>(missionRequestDto, headers);
        ResponseEntity<Map> exchange = restTemplate.exchange(url, HttpMethod.POST, entity, Map.class);
        Long id = Long.valueOf((Integer) exchange.getBody().get("id"));

        await()
                .atMost(1, TimeUnit.MINUTES)
                .pollInterval(500, TimeUnit.MILLISECONDS)
                .until(() -> {
                    MissionEntity mission = missionRepository.findById(id).get();

                    boolean ongoingReached = mission.getStatus().equals(MissionStatus.ONGOING)
                            || mission.getStatus().equals(MissionStatus.COMPLETED);
                    boolean completedReached = mission.getStatus().equals(MissionStatus.COMPLETED);
                    return ongoingReached && completedReached;
                });
    }
}
