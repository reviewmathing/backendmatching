package com.hunko.missionmatching.core.domain;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class UserReader {
    public List<User> loadFrom(List<Long> ids) {
        return List.of();
    }

    public Optional<User> loadFrom(Long id) {
        return Optional.empty();
    }
}
