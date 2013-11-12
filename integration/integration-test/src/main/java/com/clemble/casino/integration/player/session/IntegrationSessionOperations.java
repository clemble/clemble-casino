package com.clemble.casino.integration.player.session;

import static com.google.common.base.Preconditions.checkNotNull;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.clemble.casino.integration.player.Player;
import com.clemble.casino.player.security.PlayerSession;
import com.clemble.casino.web.management.ManagementWebMapping;

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
        header.add("playerId", String.valueOf(player.getPlayer()));
        header.add("Content-Type", "application/json");
        // Step 2. Generating request
        HttpEntity<Void> requestEntity = new HttpEntity<Void>(null, header);
        // Step 3. Rest template generation
        return restTemplate.exchange(baseUrl + ManagementWebMapping.MANAGEMENT_PLAYER_SESSIONS, HttpMethod.POST, requestEntity,
                PlayerSession.class, player.getPlayer()).getBody();

    }

    @Override
    public void end(Player player, long session) {
        // Step 1. Generating request
        HttpEntity<Void> requestEntity = player.<Void> sign(null);
        // Step 2. Calling appropriate services
        restTemplate.exchange(baseUrl + ManagementWebMapping.MANAGEMENT_PLAYER_SESSIONS_SESSION, HttpMethod.DELETE, requestEntity,
                PlayerSession.class, player.getPlayer(), session).getBody();
    }

    @Override
    public PlayerSession refresh(Player player, long session) {
        // Step 1. Generating request
        HttpEntity<Void> requestEntity = player.<Void> sign(null);
        // Step 2. Rest template generation
        String refreshUrl = baseUrl + ManagementWebMapping.MANAGEMENT_PLAYER_SESSIONS_SESSION;
        return restTemplate.exchange(refreshUrl, HttpMethod.PUT, requestEntity, PlayerSession.class, player.getPlayer(), session).getBody();
    }

    @Override
    public PlayerSession get(Player player, long session) {
        // Step 1. Generating request
        HttpEntity<Void> requestEntity = player.<Void> sign(null);
        // Step 2. Rest template generation
        String refreshUrl = baseUrl + ManagementWebMapping.MANAGEMENT_PLAYER_SESSIONS_SESSION;
        return restTemplate.exchange(refreshUrl, HttpMethod.GET, requestEntity, PlayerSession.class, player.getPlayer(), session).getBody();
    }

}