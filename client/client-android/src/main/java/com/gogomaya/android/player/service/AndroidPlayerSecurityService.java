package com.gogomaya.android.player.service;

import java.io.Serializable;

import org.springframework.http.HttpEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gogomaya.client.player.service.PlayerSecurityClientService;
import com.gogomaya.player.security.PlayerIdentity;

public class AndroidPlayerSecurityService implements PlayerSecurityClientService<HttpEntity<?>> {

    final private long playerId;
    final private ObjectMapper objectMapper;
    final private PlayerIdentity playerIdentity;

    public AndroidPlayerSecurityService(long playerId, PlayerIdentity playerIdentity, ObjectMapper objectMapper) {
        this.playerId = playerId;
        this.objectMapper = objectMapper;
        this.playerIdentity = playerIdentity;
    }

    @Override
    @SuppressWarnings("unchecked")
    public HttpEntity<?> signGet(String request) {
        // Step 1. Creating Header
        MultiValueMap<String, String> header = new LinkedMultiValueMap<String, String>();
        header.add("playerId", String.valueOf(playerId));
        header.add("Content-Type", "application/json");
        // Step 2. Generating request
        return new HttpEntity(header);
    }

    @Override
    public HttpEntity<?> signUpdate(Serializable request) {
        // Step 1. Creating Header
        MultiValueMap<String, String> header = new LinkedMultiValueMap<String, String>();
        header.add("playerId", String.valueOf(playerId));
        header.add("Content-Type", "application/json");
        // Step 2. Generating request
        try {
            return new HttpEntity(objectMapper.writeValueAsString(request), header);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
