package com.clemble.casino.integration.player.session;

import static com.google.common.base.Preconditions.checkNotNull;

import static com.clemble.casino.web.player.PlayerWebMapping.*;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.clemble.casino.player.security.PlayerSession;
import com.clemble.casino.player.service.PlayerSessionService;

public class IntegrationSessionService implements PlayerSessionService {

    final public RestTemplate restTemplate;
    final public String baseUrl;

    public IntegrationSessionService(RestTemplate restTemplate, String baseUrl) {
        this.restTemplate = checkNotNull(restTemplate);
        this.baseUrl = checkNotNull(baseUrl);
    }

    @Override
    public PlayerSession create(String player) {
        // Step 1. Creating Header
        MultiValueMap<String, String> header = new LinkedMultiValueMap<String, String>();
        header.add("playerId", String.valueOf(player));
        header.add("Content-Type", "application/json");
        // Step 2. Generating request
        HttpEntity<Void> requestEntity = new HttpEntity<Void>(null, header);
        // Step 3. Rest template generation
        return restTemplate.exchange(baseUrl + PRESENCE_PREFIX + PRESENCE_SESSIONS_PLAYER, HttpMethod.POST, requestEntity,
                PlayerSession.class, player).getBody();
    }

    @Override
    public PlayerSession refreshPlayerSession(String player, String sessionId) {
        String refreshUrl = baseUrl + PRESENCE_PREFIX + PRESENCE_SESSIONS_PLAYER_SESSION;
        return restTemplate.exchange(refreshUrl, HttpMethod.PUT, null, PlayerSession.class, player, sessionId).getBody();

    }

    @Override
    public void endPlayerSession(String player, String sessionId) {
        restTemplate.exchange(baseUrl + PRESENCE_PREFIX + PRESENCE_SESSIONS_PLAYER_SESSION, HttpMethod.DELETE, null,
                PlayerSession.class, player, sessionId).getBody();
    }

    @Override
    public PlayerSession getPlayerSession(String player, String sessionId) {
        String refreshUrl = baseUrl + PRESENCE_PREFIX + PRESENCE_SESSIONS_PLAYER_SESSION;
        return restTemplate.exchange(refreshUrl, HttpMethod.GET, null, PlayerSession.class, player, sessionId).getBody();
    }

}
