package com.hunko.missionmatching.core.presentation.dto;

import com.hunko.missionmatching.core.domain.GithubUri;
import jakarta.validation.constraints.NotBlank;

public record UpdateGithubUriDto(@NotBlank String uri) {
    public GithubUri toGithubUri() {
        return GithubUri.of(uri);
    }
}
