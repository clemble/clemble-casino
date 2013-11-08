package com.clemble.casino.android.player.service;

import org.springframework.http.HttpEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.clemble.casino.player.security.PlayerToken;
import com.clemble.casino.web.mapping.WebMapping;
import com.clemble.casino.client.player.service.PlayerSecurityClientService;

public class AndroidPlayerSecurityService implements PlayerSecurityClientService<HttpEntity<?>> {

    /**
     * 
     */
    private static final long serialVersionUID = -3770963835672665720L;

    final private PlayerToken playerIdentity;

    public AndroidPlayerSecurityService(PlayerToken playerIdentity) {
        this.playerIdentity = playerIdentity;
    }

    @Override
    public String getPlayer() {
        return playerIdentity.getPlayer();
    }

    @Override
    public <S> HttpEntity<?> signCreate(S request) {
        // Step 1. Creating Header
        MultiValueMap<String, String> header = new LinkedMultiValueMap<String, String>();
        header.add("playerId", String.valueOf(playerIdentity.getPlayer()));
        header.add("Content-Type", WebMapping.PRODUCES);
        // Step 2. Generating request
        return new HttpEntity<>(request, header);
    }

    @Override
    public HttpEntity<?> signRead(String request) {
        // Step 1. Creating Header
        MultiValueMap<String, String> header = new LinkedMultiValueMap<String, String>();
        header.add("playerId", String.valueOf(playerIdentity.getPlayer()));
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
