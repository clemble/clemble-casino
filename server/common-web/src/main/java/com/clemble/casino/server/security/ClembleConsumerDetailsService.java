package com.clemble.casino.server.security;

import static com.clemble.casino.utils.Preconditions.checkNotNull;

import java.io.IOException;

import org.apache.commons.lang3.SerializationException;
import org.springframework.security.oauth.common.OAuthException;
import org.springframework.security.oauth.provider.ConsumerDetailsService;

import com.couchbase.client.CouchbaseClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ClembleConsumerDetailsService implements ConsumerDetailsService {

    final private CouchbaseClient couchbaseClient;
    final private ObjectMapper objectMapper;

    public ClembleConsumerDetailsService(CouchbaseClient couchbaseClient, ObjectMapper objectMapper) {
        this.objectMapper = checkNotNull(objectMapper);
        this.couchbaseClient = checkNotNull(couchbaseClient);
    }

    @Override
    public ClembleConsumerDetails loadConsumerByConsumerKey(String consumerKey) throws OAuthException {
        // Step 1. Sanity Check
        if (consumerKey == null)
            return null;
        // Step 2. Fetching documentation
        Object documentation = couchbaseClient.get(consumerKey);
        // Step 3. Read value
        try {
            return objectMapper.readValue((String) documentation, ClembleConsumerDetails.class);
        } catch (IOException e) {
            throw new SerializationException("Failed to read customer details", e);
        }
    }

    public void save(ClembleConsumerDetails clembleConsumerDetails) {
        // Step 1. Sanity check
        if (clembleConsumerDetails == null || clembleConsumerDetails.getConsumerKey() == null)
            return;
        // Step 2. Performing actual saving
        try {
            String json = objectMapper.writeValueAsString(clembleConsumerDetails);
            couchbaseClient.set(clembleConsumerDetails.getConsumerKey(), json);
        } catch (JsonProcessingException e) {
            throw new SerializationException("Failed to write customer details", e);
        }
    }

}
