package com.gogomaya.server.integration.util;

import java.io.IOException;

import org.junit.Assert;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gogomaya.server.error.GogomayaException;
import com.gogomaya.server.error.GogomayaFailure;

public class GogomayaHTTPErrorHandler implements ResponseErrorHandler {

    final ObjectMapper objectMapper = new ObjectMapper();
    
    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        return response.getStatusCode().value() >= 400;
    }

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        // Step 1. Checking that response is of JSON type
        Assert.assertTrue(response.getHeaders().getContentType().toString().contains(MediaType.APPLICATION_JSON_VALUE));
        // Step 2. Converting to GogomayaFailure
        byte[] buffer = new byte[response.getBody().available()];
        response.getBody().read(buffer);
        String errorMessage = new String(buffer);
        GogomayaFailure failure = objectMapper.readValue(errorMessage, GogomayaFailure.class);
        // Step 3. Generating GogomayaException
        throw GogomayaException.create(failure);
    }
}