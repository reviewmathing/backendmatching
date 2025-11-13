package com.hunko.missionmatching.core.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.hunko.missionmatching.core.exception.CoreException;
import java.net.URI;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class GithubUriTest {

    @ParameterizedTest
    @ValueSource(strings = {"http://", "https://", "http://www.", "https://www."})
    void 깃허브URL생성(String prefix) {
        String uri = prefix + "github.com/woowacourse-precourse/java-lotto-8";

        GithubUri githubUri = GithubUri.of(uri);

        assertThat(githubUri.toUri()).isEqualTo(URI.create(uri));
    }

    @ParameterizedTest
    @ValueSource(strings = {"http", "https"})
    void 깃허브가아닌URL생성_실패(String scheme) {
        String uri = scheme + "://" + "github.coma/woowacourse-precourse/java-lotto-8";

        assertThatThrownBy(() -> GithubUri.of(uri)).isInstanceOf(CoreException.class);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "http://github.com/woowacourse-precourse/java-lotto-8/test",
            "http://github.com/woowacourse-precourse/java-lotto-8",
            "http://github.com/woowacourse-precourse/java-lotto-8/",
            "https://github.com/woowacourse-precourse/java-lotto-8/test",
            "https://github.com/woowacourse-precourse/java-lotto-8",
            "https://github.com/woowacourse-precourse/java-lotto-8/",
            "https://www.github.com/woowacourse-precourse/java-lotto-8/test",
            "https://www.github.com/woowacourse-precourse/java-lotto-8",
            "https://www.github.com/woowacourse-precourse/java-lotto-8/",
            "http://www.github.com/woowacourse-precourse/java-lotto-8/test",
            "http://www.github.com/woowacourse-precourse/java-lotto-8",
            "http://www.github.com/woowacourse-precourse/java-lotto-8/",
    })
    void 하위URL검증(String path) {
        String uri = "http://github.com/woowacourse-precourse/java-lotto-8/test";
        GithubUri githubUri = GithubUri.of(uri);

        githubUri.isSubUrl(GithubUri.of(path));
    }
}