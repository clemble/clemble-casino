package com.clemble.casino.server.security;

import static com.clemble.casino.utils.Preconditions.checkNotNull;

import java.io.IOException;

import org.apache.commons.lang3.SerializationException;
import org.springframework.security.oauth.common.OAuthException;
import org.springframework.security.oauth.common.signature.RSAKeySecret;
import org.springframework.security.oauth.provider.ConsumerDetailsService;

import com.clemble.casino.player.client.ClembleConsumerDetails;
import com.couchbase.client.CouchbaseClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ClembleCouchbaseConsumerDetailsService implements ClembleConsumerDetailsService {

    final private ObjectMapper objectMapper;
    final private CouchbaseClient couchbaseClient;

    public ClembleCouchbaseConsumerDetailsService(CouchbaseClient couchbaseClient, ObjectMapper objectMapper) {
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
            throw new IllegalArgumentException("Invalid ClembleConsumerDetails");
        // Step 1.1 Removing private key from consumer details they must not be stored on the server
        if (clembleConsumerDetails.getSignatureSecret().getPrivateKey() != null) {;
            clembleConsumerDetails = new ClembleConsumerDetails(clembleConsumerDetails.getConsumerKey(), 
                    clembleConsumerDetails.getConsumerName(), 
                    new RSAKeySecret(clembleConsumerDetails.getSignatureSecret().getPublicKey()),
                    clembleConsumerDetails.getAuthorities(),
                    clembleConsumerDetails.getClientDetail());
        }
        // Step 2. Performing actual saving
        try {
            String json = objectMapper.writeValueAsString(clembleConsumerDetails);
            couchbaseClient.set(clembleConsumerDetails.getConsumerKey(), json);
        } catch (JsonProcessingException e) {
            throw new SerializationException("Failed to write customer details", e);
        }
    }

}
