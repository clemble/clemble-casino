package com.clemble.casino.android;

import java.net.URI;

import org.springframework.social.MissingAuthorizationException;
import org.springframework.social.support.URIBuilder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

abstract public class AbstractClembleCasinoOperations {

    private final String apiBase;
    private final boolean isUserAuthorized;
    private final boolean isAppAuthorized;

    public AbstractClembleCasinoOperations(boolean isUserAuthorized, boolean isAppAuthorized, String apiBase) {
        this.isUserAuthorized = isUserAuthorized;
        this.isAppAuthorized = isAppAuthorized;
        this.apiBase = apiBase;
    }

    protected void requireUserAuthorization() {
        if (!isUserAuthorized) {
            throw new MissingAuthorizationException("clemble");
        }
    }

    protected void requireAppAuthorization() {
        if (!isAppAuthorized) {
            throw new MissingAuthorizationException("clemble");
        }
    }

    protected void requireEitherUserOrAppAuthorization() {
        if (!isUserAuthorized && !isAppAuthorized) {
            throw new MissingAuthorizationException("twitter");
        }
    }

    protected URI buildUri(String path) {
        return buildUri(path, EMPTY_PARAMETERS);
    }

    protected URI buildUri(String path, String parameterName, String parameterValue) {
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<String, String>();
        parameters.set(parameterName, parameterValue);
        return buildUri(path, parameters);
    }

    protected URI buildUri(String path, MultiValueMap<String, String> parameters) {
        return URIBuilder.fromUri(apiBase + path).queryParams(parameters).build();
    }

    private static final LinkedMultiValueMap<String, String> EMPTY_PARAMETERS = new LinkedMultiValueMap<String, String>();

}
