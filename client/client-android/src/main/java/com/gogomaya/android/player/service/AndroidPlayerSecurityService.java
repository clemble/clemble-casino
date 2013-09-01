package com.gogomaya.android.player.service;

import org.springframework.http.HttpEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.gogomaya.client.player.service.PlayerSecurityClientService;
import com.gogomaya.player.security.PlayerIdentity;

public class AndroidPlayerSecurityService implements PlayerSecurityClientService {

    final private PlayerIdentity playerIdentity;

    public AndroidPlayerSecurityService(PlayerIdentity playerIdentity) {
        this.playerIdentity = playerIdentity;
    }

    @Override
    @SuppressWarnings("unchecked")
    public HttpEntity<?> signGet(String request) {
        // Step 1. Creating Header
        MultiValueMap<String, String> header = new LinkedMultiValueMap<String, String>();
        header.add("playerId", String.valueOf(playerIdentity.getPlayerId()));
        header.add("Content-Type", "application/json");
        // Step 2. Generating request
        return new HttpEntity(header);
    }

    @Override
    public HttpEntity<?> signUpdate(Object request) {
        // Step 1. Creating Header
        MultiValueMap<String, String> header = new LinkedMultiValueMap<String, String>();
        header.add("playerId", String.valueOf(playerIdentity.getPlayerId()));
        header.add("Content-Type", "application/json");
        // Step 2. Generating request
        return new HttpEntity(request, header);
    }

}
