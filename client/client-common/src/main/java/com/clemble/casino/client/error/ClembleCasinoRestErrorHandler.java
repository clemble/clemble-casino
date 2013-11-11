package com.clemble.casino.client.error;

import java.io.IOException;

import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.clemble.casino.error.ClembleCasinoException;
import com.clemble.casino.error.ClembleCasinoFailureDescription;

public class ClembleCasinoRestErrorHandler implements ResponseErrorHandler {

    final ObjectMapper objectMapper;

    public ClembleCasinoRestErrorHandler(ObjectMapper objectMapper) {
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
        ClembleCasinoFailureDescription description = objectMapper.readValue(errorMessage, ClembleCasinoFailureDescription.class);
        // Step 3. Generating GogomayaException
        throw ClembleCasinoException.fromDescription(description);
    }
}
