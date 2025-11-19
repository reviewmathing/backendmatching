package com.hunko.missionmatching.core.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@Slf4j
public class UserReader {

    private final RestTemplate restTemplate;

    private final String userHostUrl;

    private final ObjectMapper objectMapper;

    public UserReader(@Value("${user.host.url}") String userHostUrl, RestTemplateBuilder restTemplateBuilder,
                      ObjectMapper objectMapper) {
        this.userHostUrl = userHostUrl;
        restTemplate = restTemplateBuilder
                .readTimeout(Duration.of(1, ChronoUnit.SECONDS))
                .build();
        this.objectMapper = objectMapper;
        ;
    }

    public List<User> loadFrom(List<Long> ids) {
        try {
            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(userHostUrl + "/api/users");

            for (Long id : ids) {
                builder.queryParam("ids", id);
            }
            URI uri = builder.build().encode().toUri();
            ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);
            if (response.getStatusCode() != HttpStatus.OK) {
                log.warn("users found response status code: {} message : {}", response.getStatusCode(),
                        response.getBody());
                return Collections.emptyList();
            }
            String body = response.getBody();
            UserDto user = objectMapper.readValue(body, UserDto.class);
            return user.users;

        } catch (HttpClientErrorException e) {
            log.warn("users found response status code: {} message : {}", e.getStatusCode(),
                    e.getResponseBodyAsString());
        } catch (JsonProcessingException e) {
            log.warn("body mapping fail {}", e.getMessage());
        }
        return Collections.emptyList();

    }

    public Optional<User> loadFrom(Long id) {
        List<User> users = loadFrom(List.of(id));
        if (users.isEmpty()) {
            return Optional.empty();
        }
        if (users.size() > 1) {
            log.warn("multiple users found for id {}", id);
            return Optional.empty();
        }
        return Optional.of(users.getFirst());
    }

    @Getter
    @NoArgsConstructor
    private static class UserDto {
        private List<User> users;
    }
}
