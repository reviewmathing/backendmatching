package com.hunko.missionmatching.core.presentaion;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.hunko.missionmatching.core.domain.GithubUri;
import com.hunko.missionmatching.core.domain.Mission;
import com.hunko.missionmatching.core.domain.MissionStatus;
import com.hunko.missionmatching.core.domain.TestMissionFactory;
import com.hunko.missionmatching.core.domain.UserReader;
import com.hunko.missionmatching.core.presentation.dto.MissionDto;
import com.hunko.missionmatching.core.presentation.dto.MissionPageDto;
import com.hunko.missionmatching.core.scheduler.MissionSchedulerWalFactory;
import com.hunko.missionmatching.helper.RequestBuildersHelper;
import com.hunko.missionmatching.helper.StubFailMissionWalFactory;
import com.hunko.missionmatching.storage.MissionEntity;
import com.hunko.missionmatching.storage.MissionMapper;
import com.hunko.missionmatching.storage.MissionRepository;
import com.jayway.jsonpath.JsonPath;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
public class MissionIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
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
    public void 미션생성성공() throws Exception {
        String title = "test";
        LocalDateTime startDateTime = LocalDateTime.now();
        LocalDateTime endDateTime = startDateTime.plusDays(1);
        Long creatorId = 1L;
        String zone = ZoneId.systemDefault().toString();
        String url = "https://github.com/woowacourse-precourse/java-lotto-8";
        Map<String, ? extends Serializable> requestBody = Map.of(
                "title", title,
                "startDateTime", startDateTime,
                "endDateTime", endDateTime,
                "zone", zone,
                "github", url
        );

        MvcResult result = mockMvc.perform(
                        RequestBuildersHelper.post("/api/missions")
                                .authentication(creatorId.toString(), "ADMIN")
                                .content(requestBody)
                )
                .andReturn();

        assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());
        String response = result.getResponse().getContentAsString();
        Long id = Long.parseLong(JsonPath.read(response, "$.id").toString());
        assertThat(missionRepository.findById(id))
                .isPresent()
                .get()
                .hasFieldOrPropertyWithValue("id", id)
                .hasFieldOrPropertyWithValue("title", title)
                .hasFieldOrPropertyWithValue("startDate", startDateTime.atZone(ZoneId.systemDefault()))
                .hasFieldOrPropertyWithValue("endDate", endDateTime.atZone(ZoneId.systemDefault()))
                .hasFieldOrPropertyWithValue("creator", creatorId);
    }

    @Test
    public void 미션생성_실패_권한없음() throws Exception {
        String title = "test";
        LocalDateTime startDateTime = LocalDateTime.now();
        LocalDateTime endDateTime = startDateTime.plusDays(1);
        Long creatorId = 1L;
        String zone = ZoneId.systemDefault().toString();
        String url = "https://github.com/woowacourse-precourse/java-lotto-8";
        Map<String, ? extends Serializable> requestBody = Map.of(
                "title", title,
                "startDateTime", startDateTime,
                "endDateTime", endDateTime,
                "zone", zone,
                "github", url
        );

        MvcResult result = mockMvc.perform(
                        RequestBuildersHelper.post("/api/missions")
                                .authentication(creatorId.toString(), "USER")
                                .content(requestBody)
                )
                .andReturn();

        assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.FORBIDDEN.value());
    }

    @Test
    public void 미션생성_실패_권한없음_비로그인() throws Exception {
        String title = "test";
        LocalDateTime startDateTime = LocalDateTime.now();
        LocalDateTime endDateTime = startDateTime.plusDays(1);
        String zone = ZoneId.systemDefault().toString();
        String url = "https://github.com/woowacourse-precourse/java-lotto-8";
        Map<String, ? extends Serializable> requestBody = Map.of(
                "title", title,
                "startDateTime", startDateTime,
                "endDateTime", endDateTime,
                "zone", zone,
                "github", url
        );

        MvcResult result = mockMvc.perform(
                        RequestBuildersHelper.post("/api/missions")
                                .nonAuthenticated()
                                .content(requestBody)
                )
                .andReturn();

        assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.FORBIDDEN.value());
    }

    @Test
    public void 어드민_미션조회() throws Exception {

        Mission ongoing = TestMissionFactory.createMission(null, MissionStatus.ONGOING, 1L);
        Mission pending = TestMissionFactory.createMission(null, MissionStatus.PENDING, 1L);
        Mission completed = TestMissionFactory.createMission(null, MissionStatus.COMPLETED, 1L);

        List<Mission> missions = saveMissions(ongoing, ongoing, pending, pending, completed);
        Mission cursor = missions.get(1);

        MvcResult result = mockMvc.perform(
                        RequestBuildersHelper.get("/api/missions")
                                .authentication(String.valueOf(1), "ADMIN")
                                .param("id", cursor.getId().toLong())
                                .param("limit", 20)
                                .param("start", cursor.getTimePeriod().getOriginStartDate())
                                .param("end", cursor.getTimePeriod().getOriginEndDate())
                                .param("zone", cursor.getTimePeriod().getZoneId())
                                .param("status", cursor.getStatus())
                ).andDo(print())
                .andReturn();

        assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        MissionPageDto missionPageDto = mapper.readValue(result.getResponse().getContentAsString(),
                MissionPageDto.class);
        assertThat(missionPageDto.missions()).hasSize(3);
        List<Long> ids = missions.subList(2, missions.size()).stream().map((m) -> m.getId().toLong()).toList();
        assertThat(missionPageDto.missions().stream().map(MissionDto::id)).containsExactlyElementsOf(ids);
    }

    @Test
    public void 사용자_미션업데이트() throws Exception {

        Mission pending = TestMissionFactory.createMission(null, MissionStatus.PENDING, 1L);

        List<Mission> missions = saveMissions(pending);
        Mission mission = missions.getFirst();

        String title = "test2";
        LocalDateTime startDateTime = LocalDateTime.now();
        LocalDateTime endDateTime = startDateTime.plusDays(2);
        String zone = ZoneId.systemDefault().toString();
        String url = "https://github.com/woowacourse-precourse/java-lotto-8";
        Map<String, ? extends Serializable> requestBody = Map.of(
                "title", title,
                "startDateTime", startDateTime,
                "endDateTime", endDateTime,
                "zone", zone,
                "github", url
        );

        MvcResult result = mockMvc.perform(
                        RequestBuildersHelper.put("/api/missions/" + mission.getId().toLong())
                                .authentication(String.valueOf(1), "Admin")
                                .content(requestBody)

                ).andDo(print())
                .andReturn();

        assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());
        Mission updatedMission = MissionMapper.toMission(missionRepository.findById(mission.getId().toLong()).get());
        assertThat(updatedMission.getTitle()).isEqualTo("test2");
        assertThat(updatedMission.getTimePeriod().getStartDate()).isEqualTo(startDateTime.atZone(ZoneId.of(zone)));
        assertThat(updatedMission.getTimePeriod().getEndDate()).isEqualTo(endDateTime.atZone(ZoneId.of(zone)));
        assertThat(updatedMission.getMissionUrl()).isEqualTo(GithubUri.of(url));
    }

    @Test
    public void 사용자_미션업데이트_권한없음() throws Exception {

        Mission pending = TestMissionFactory.createMission(null, MissionStatus.PENDING, 1L);

        List<Mission> missions = saveMissions(pending);
        Mission mission = missions.getFirst();

        String title = "test2";
        LocalDateTime startDateTime = LocalDateTime.now();
        LocalDateTime endDateTime = startDateTime.plusDays(2);
        String zone = ZoneId.systemDefault().toString();
        String url = "https://github.com/woowacourse-precourse/java-lotto-8";
        Map<String, ? extends Serializable> requestBody = Map.of(
                "title", title,
                "startDateTime", startDateTime,
                "endDateTime", endDateTime,
                "zone", zone,
                "github", url
        );

        MvcResult result = mockMvc.perform(
                        RequestBuildersHelper.put("/api/missions/" + mission.getId().toLong())
                                .authentication(String.valueOf(1), "USER")
                                .content(requestBody)

                ).andDo(print())
                .andReturn();

        assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.FORBIDDEN.value());
    }

    @Test
    void 사용자_미션조회() throws Exception {

        Mission ongoing = TestMissionFactory.createMission(null, MissionStatus.ONGOING, 1L);
        Mission pending = TestMissionFactory.createMission(null, MissionStatus.PENDING, 1L);
        Mission completed = TestMissionFactory.createMission(null, MissionStatus.COMPLETED, 1L);

        List<Mission> missions = saveMissions(ongoing, ongoing, pending, pending, completed);
        Mission cursor = missions.get(1);

        MvcResult result = mockMvc.perform(
                        RequestBuildersHelper.get("/api/missions")
                                .authentication(String.valueOf(1), "USER")
                                .param("id", cursor.getId().toLong())
                                .param("limit", 20)
                                .param("start", cursor.getTimePeriod().getOriginStartDate())
                                .param("end", cursor.getTimePeriod().getOriginEndDate())
                                .param("zone", ZoneId.systemDefault())
                                .param("status", cursor.getStatus())
                ).andDo(print())
                .andReturn();

        assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        MissionPageDto missionPageDto = mapper.readValue(result.getResponse().getContentAsString(),
                MissionPageDto.class);
        assertThat(missionPageDto.missions()).hasSize(0);
    }

    private List<Mission> saveMissions(Mission... missions) {
        List<Mission> result = new ArrayList<>(missions.length);
        for (Mission mission : missions) {
            MissionEntity save = missionRepository.save(MissionMapper.toMissionEntity(mission));
            result.add(MissionMapper.toMission(save));
        }
        return result;
    }
}
