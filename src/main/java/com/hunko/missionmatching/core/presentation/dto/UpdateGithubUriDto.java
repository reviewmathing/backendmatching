package com.hunko.missionmatching.core.presentation.dto;

import com.hunko.missionmatching.core.domain.GithubUri;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record UpdateGithubUriDto(@Min(1)@Max(5) Integer reviewCount, @NotBlank String uri) {
    public GithubUri toGithubUri() {
        return GithubUri.of(uri);
    }
}
