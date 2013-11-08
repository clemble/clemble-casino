package com.clemble.casino.server.security;

import static com.clemble.casino.utils.Preconditions.checkNotNull;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.security.oauth.provider.token.OAuthProviderTokenImpl;
import org.springframework.security.oauth.provider.token.RandomValueProviderTokenServices;

import com.couchbase.client.CouchbaseClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ClembleOAuthProviderTokenServices extends RandomValueProviderTokenServices {

    // TODO track unused tokens

    final private ObjectMapper objectMapper;
    final private CouchbaseClient couchbaseClient;

    public ClembleOAuthProviderTokenServices(CouchbaseClient couchbaseClient, ObjectMapper objectMapper) {
        this.objectMapper = checkNotNull(objectMapper);
        this.couchbaseClient = checkNotNull(couchbaseClient);
        this.setAccessTokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(7));
    }

    @Override
    protected OAuthProviderTokenImpl readToken(String token) {
        // Step 1. Sanity check
        Object document = couchbaseClient.get(token);
        if (document == null)
            return null;
        // Step 2. Reading returned value
        try {
            return objectMapper.readValue((String) document, OAuthProviderTokenImpl.class);
        } catch (IOException e) {
            throw new SerializationException("Failed to read token " + token, e);
        }
    }

    @Override
    protected void storeToken(String tokenValue, OAuthProviderTokenImpl token) {
        // Step 1. Sanity check
        if (token == null || tokenValue == null)
            return;
        // Step 2. Checking Couchbase client
        try {
            couchbaseClient.set(tokenValue, objectMapper.writeValueAsString(token));
        } catch (JsonProcessingException e) {
            throw new SerializationException("Failed to write token " + tokenValue, e);
        }
    }

    @Override
    protected OAuthProviderTokenImpl removeToken(String tokenValue) {
        if (tokenValue != null)
            couchbaseClient.delete(tokenValue);
        // No need to get the previous token if it was already used
        return null;
    }

}
