package com.clemble.casino.android;

import java.net.URI;

import org.springframework.social.support.URIBuilder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.clemble.casino.ServerRegistry;

abstract public class AbstractClembleCasinoOperations {

    private final ServerRegistry apiBase;

    public AbstractClembleCasinoOperations(ServerRegistry apiBase) {
        this.apiBase = apiBase;
    }

    protected URI buildUri(String path){
        return URIBuilder.fromUri(apiBase.findBase() + path).build();
    }

    protected URI buildUri(String path, MultiValueMap<String, String> parameters) {
        return URIBuilder.fromUri(apiBase.findBase() + path).queryParams(parameters).build();
    }

    protected URI buildUriById(String selector, String path) {
        return buildUriById(selector, path, EMPTY_PARAMETERS);
    }

    protected URI buildUriById(String selector, String path, String parameterName, String parameterValue) {
        return buildUriById(selector, path, EMPTY_PARAMETERS);
    }

    protected URI buildUriById(String selector, String path, String parameterName, String parameterValue, String paramName2, String paramValue2) {
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<String, String>();
        parameters.set(parameterName, parameterValue);
        parameters.set(paramName2, paramValue2);
        return buildUriById(selector, path, parameters);
    }

    protected URI buildUriById(String selector, String path, MultiValueMap<String, String> parameters) {
        return URIBuilder.fromUri(apiBase.findById(selector) + path).queryParams(parameters).build();
    }

    private static final LinkedMultiValueMap<String, String> EMPTY_PARAMETERS = new LinkedMultiValueMap<String, String>();

}
