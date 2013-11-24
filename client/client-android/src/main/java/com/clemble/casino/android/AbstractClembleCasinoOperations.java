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

    protected URI buildUri(String path) {
        return URIBuilder.fromUri(apiBase.findBase() + path).build();
    }

    protected URI buildUri(String path, MultiValueMap<String, String> parameters) {
        return URIBuilder.fromUri(apiBase.findBase() + path).queryParams(parameters).build();
    }

    protected URI buildUriWith(String path, Object ... params) {
        return buildUriWith(path, EMPTY_PARAMETERS, toStringArray(params));
    }

    protected URI buildUriWith(String path, MultiValueMap<String, String> queryParams, String ... parameters) {
        String apiUrl = (parameters != null && parameters.length > 0 ? apiBase.findById(parameters[0]) : apiBase.findBase()) + path;
        String url = toUrl(apiUrl, parameters);
        return URIBuilder.fromUri(url).queryParams(queryParams).build();
    }

    private static final LinkedMultiValueMap<String, String> EMPTY_PARAMETERS = new LinkedMultiValueMap<String, String>();
    private static final String[] EMPTY_PATH_VARIABLES = new String[0];

    private String[] toStringArray(Object[] parameters) {
        // Step 1. Sanity check
        if(parameters == null || parameters.length == 0)
            return EMPTY_PATH_VARIABLES;
        // Step 2. Generating parameters String list
        String[] pathVariables = new String[parameters.length];
        for(int i = 0; i < pathVariables.length; i++) {
            pathVariables[i] = String.valueOf(parameters[i]);
        }
        return pathVariables;
    }
    
    private String toUrl(String url, String ... parameters) {
        char[] originalUrl = url.toCharArray();
        int paramPointer = 0;
        StringBuilder resultUrl = new StringBuilder();
        for (int i = 0; i < originalUrl.length; i++) {
            if ('{' == originalUrl[i]) {
                resultUrl.append(parameters[paramPointer++]);
                do {
                } while (originalUrl[++i] != '}');
            } else {
                resultUrl.append(originalUrl[i]);
            }
        }
        return resultUrl.toString();
    }

}
