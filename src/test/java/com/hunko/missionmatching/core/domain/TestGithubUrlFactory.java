package com.hunko.missionmatching.core.domain;

public class TestGithubUrlFactory {

    private final static String PREFIX = "https://github.com";

    private TestGithubUrlFactory() {
    }

    public static GithubUri createGithubUri() {
        return GithubUri.of(PREFIX);
    }

    public static GithubUri createGithubUri(String path) {
        if (path.startsWith("/")) {
            return GithubUri.of(PREFIX + path);
        }
        return GithubUri.of(PREFIX + "/" + path);
    }
}
