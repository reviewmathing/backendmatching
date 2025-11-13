package com.hunko.missionmatching.core.domain;

import com.hunko.missionmatching.core.exception.ErrorType;
import com.hunko.missionmatching.util.UrlConstants;
import java.net.URI;
import java.util.Objects;

public class GithubUri {

    private static final String HOST = "github.com";

    private final URI url;

    public GithubUri(URI url) {
        if (url == null || !isSupportedHost(url.getHost())) {
            ErrorType.INVALID_INPUT.throwException();
        }
        this.url = url;
    }

    public static GithubUri of(String url) {
        return new GithubUri(URI.create(url));
    }

    private boolean isSupportedHost(String host) {
        if (host == null) {
            return false;
        }
        return host.equals(HOST) || host.endsWith(UrlConstants.DOT + HOST);
    }

    public boolean isSubUrl(GithubUri subUrl) {
        String subRawPath = subUrl.url.getRawPath();
        String parentRawPath = this.url.getRawPath();
        if (subRawPath.equals(parentRawPath)) {
            return true;
        }
        return subRawPath.startsWith(parentRawPath) && (parentRawPath.endsWith(UrlConstants.SLASH)
                || String.valueOf(subRawPath.charAt(parentRawPath.length())).endsWith(UrlConstants.SLASH));
    }

    public String toUriString() {
        return url.toString();
    }

    public URI toUri() {
        return url;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GithubUri githubUri = (GithubUri) o;
        return Objects.equals(url, githubUri.url);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(url);
    }
}
