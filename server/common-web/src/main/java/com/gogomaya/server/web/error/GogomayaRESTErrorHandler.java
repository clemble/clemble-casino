package com.gogomaya.server.web.error;

import java.io.IOException;

import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gogomaya.server.error.GogomayaException;
import com.gogomaya.server.error.GogomayaFailureDescription;

public class GogomayaRESTErrorHandler implements ResponseErrorHandler {

    final ObjectMapper objectMapper;

    public GogomayaRESTErrorHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        return response.getStatusCode().value() >= 400;
    }

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        // Step 1. Reading error string
        byte[] buffer = new byte[response.getBody().available()];
        response.getBody().read(buffer);
        String errorMessage = new String(buffer);
        // Step 2. Checking that response is of JSON type
        assert response.getHeaders().getContentType().toString().contains(MediaType.APPLICATION_JSON_VALUE) : errorMessage;
        GogomayaFailureDescription description = objectMapper.readValue(errorMessage, GogomayaFailureDescription.class);
        // Step 3. Generating GogomayaException
        throw GogomayaException.fromDescription(description);
    }
}
