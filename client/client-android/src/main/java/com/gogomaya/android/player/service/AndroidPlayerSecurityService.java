package com.gogomaya.android.player.service;

import org.springframework.http.HttpEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.gogomaya.client.player.service.PlayerSecurityClientService;
import com.gogomaya.player.security.PlayerIdentity;
import com.gogomaya.web.mapping.WebMapping;

public class AndroidPlayerSecurityService implements PlayerSecurityClientService<HttpEntity<?>> {

    /**
     * 
     */
    private static final long serialVersionUID = -3770963835672665720L;

    final private PlayerIdentity playerIdentity;

    public AndroidPlayerSecurityService(PlayerIdentity playerIdentity) {
        this.playerIdentity = playerIdentity;
    }

    @Override
    public long getPlayerId() {
        return playerIdentity.getPlayerId();
    }

    @Override
    public <S> HttpEntity<?> signCreate(S request) {
        // Step 1. Creating Header
        MultiValueMap<String, String> header = new LinkedMultiValueMap<String, String>();
        header.add("playerId", String.valueOf(playerIdentity.getPlayerId()));
        header.add("Content-Type", WebMapping.PRODUCES);
        // Step 2. Generating request
        return new HttpEntity<>(request, header);
    }

    @Override
    public HttpEntity<?> signRead(String request) {
        // Step 1. Creating Header
        MultiValueMap<String, String> header = new LinkedMultiValueMap<String, String>();
        header.add("playerId", String.valueOf(playerIdentity.getPlayerId()));
        header.add("Content-Type", WebMapping.PRODUCES);
        // Step 2. Generating request
        return new HttpEntity<>(header);
    }

    @Override
    public HttpEntity<?> signUpdate(Object request) {
        return signCreate(request);
    }

    @Override
    public HttpEntity<?> signDelete(String request) {
        return signRead(request);
    }
}
