package com.clemble.casino.client.error;

import java.io.IOException;

import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

import com.clemble.casino.error.ClembleCasinoException;
import com.clemble.casino.error.ClembleCasinoFailureDescription;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ClembleCasinoErrorHandler implements ResponseErrorHandler {

    final ObjectMapper objectMapper;

    public ClembleCasinoErrorHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        return response.getStatusCode().value() >= 400;
    }

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        int contentLength = Math.max(response.getBody().available(), (int) response.getHeaders().getContentLength()); 
        // Step 1. Reading error string
        byte[] buffer = new byte[contentLength];
        response.getBody().read(buffer);
        String errorMessage = new String(buffer);
        // Step 2. Checking that response is of JSON type
        ClembleCasinoFailureDescription description = objectMapper.readValue(errorMessage, ClembleCasinoFailureDescription.class);
        // Step 3. Generating GogomayaException
        throw ClembleCasinoException.fromDescription(description);
    }
}
