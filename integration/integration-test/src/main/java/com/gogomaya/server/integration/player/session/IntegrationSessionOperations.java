package com.gogomaya.server.integration.player.session;

import static com.google.common.base.Preconditions.checkNotNull;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.gogomaya.player.security.PlayerSession;
import com.gogomaya.server.integration.player.Player;
import com.gogomaya.web.management.ManagementWebMapping;

public class IntegrationSessionOperations extends AbstractSessionOperations {

    final public RestTemplate restTemplate;
    final public String baseUrl;

    public IntegrationSessionOperations(RestTemplate restTemplate, String baseUrl) {
        this.restTemplate = checkNotNull(restTemplate);
        this.baseUrl = checkNotNull(baseUrl);
    }

    @Override
    public PlayerSession start(Player player) {
        // Step 1. Creating Header
        MultiValueMap<String, String> header = new LinkedMultiValueMap<String, String>();
        header.add("playerId", String.valueOf(player.getPlayerId()));
        header.add("Content-Type", "application/json");
        // Step 2. Generating request
        HttpEntity<Void> requestEntity = new HttpEntity<Void>(null, header);
        // Step 3. Rest template generation
        return restTemplate.exchange(baseUrl + ManagementWebMapping.MANAGEMENT_PLAYER_SESSIONS, HttpMethod.POST, requestEntity,
                PlayerSession.class, player.getPlayerId()).getBody();

    }

    @Override
    public PlayerSession end(Player player, long session) {
        // Step 1. Generating request
        HttpEntity<Void> requestEntity = player.<Void> sign(null);
        // Step 2. Calling appropriate services
        return restTemplate.exchange(baseUrl + ManagementWebMapping.MANAGEMENT_PLAYER_SESSIONS_SESSION, HttpMethod.DELETE, requestEntity,
                PlayerSession.class, player.getPlayerId(), session).getBody();

    }

    @Override
    public PlayerSession refresh(Player player, long session) {
        // Step 1. Generating request
        HttpEntity<Void> requestEntity = player.<Void> sign(null);
        // Step 2. Rest template generation
        String refreshUrl = baseUrl + ManagementWebMapping.MANAGEMENT_PLAYER_SESSIONS_SESSION;
        return restTemplate.exchange(refreshUrl, HttpMethod.PUT, requestEntity, PlayerSession.class, player.getPlayerId(), session).getBody();
    }

    @Override
    public PlayerSession get(Player player, long session) {
        // Step 1. Generating request
        HttpEntity<Void> requestEntity = player.<Void> sign(null);
        // Step 2. Rest template generation
        String refreshUrl = baseUrl + ManagementWebMapping.MANAGEMENT_PLAYER_SESSIONS_SESSION;
        return restTemplate.exchange(refreshUrl, HttpMethod.GET, requestEntity, PlayerSession.class, player.getPlayerId(), session).getBody();
    }

}