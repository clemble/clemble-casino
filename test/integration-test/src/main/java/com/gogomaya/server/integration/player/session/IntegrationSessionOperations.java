package com.gogomaya.server.integration.player.session;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.gogomaya.server.integration.player.Player;
import com.gogomaya.server.player.security.PlayerSession;
import com.gogomaya.server.web.mapping.PlayerWebMapping;

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
        return restTemplate.exchange(baseUrl + PlayerWebMapping.PLAYER_PREFIX + PlayerWebMapping.PLAYER_SESSIONS, HttpMethod.POST, requestEntity,
                PlayerSession.class, player.getPlayerId()).getBody();

    }

    @Override
    public PlayerSession end(Player player, long session) {
        // Step 1. Creating Header
        MultiValueMap<String, String> header = new LinkedMultiValueMap<String, String>();
        header.add("playerId", String.valueOf(player.getPlayerId()));
        header.add("Content-Type", "application/json");
        // Step 2. Generating request
        HttpEntity<Void> requestEntity = new HttpEntity<Void>(null, header);
        return restTemplate.exchange(baseUrl + PlayerWebMapping.PLAYER_PREFIX + PlayerWebMapping.PLAYER_SESSIONS_SESSION, HttpMethod.DELETE, requestEntity,
                PlayerSession.class, player.getPlayerId(), session).getBody();

    }

    @Override
    public PlayerSession refresh(Player player, long session) {
        // Step 1. Creating Header
        MultiValueMap<String, String> header = new LinkedMultiValueMap<String, String>();
        header.add("playerId", String.valueOf(player.getPlayerId()));
        header.add("Content-Type", "application/json");
        // Step 2. Generating request
        HttpEntity<Void> requestEntity = new HttpEntity<Void>(null, header);
        // Step 3. Rest template generation
        String refreshUrl = baseUrl + PlayerWebMapping.PLAYER_PREFIX + PlayerWebMapping.PLAYER_SESSIONS_SESSION;
        return restTemplate.exchange(refreshUrl, HttpMethod.PUT, requestEntity, PlayerSession.class, player.getPlayerId(), session).getBody();
    }

    @Override
    public PlayerSession get(Player player, long session) {
        // Step 1. Creating Header
        MultiValueMap<String, String> header = new LinkedMultiValueMap<String, String>();
        header.add("playerId", String.valueOf(player.getPlayerId()));
        header.add("Content-Type", "application/json");
        // Step 2. Generating request
        HttpEntity<Void> requestEntity = new HttpEntity<Void>(null, header);
        // Step 3. Rest template generation
        return restTemplate.exchange(baseUrl + PlayerWebMapping.PLAYER_PREFIX + PlayerWebMapping.PLAYER_SESSIONS_SESSION, HttpMethod.GET, requestEntity,
                PlayerSession.class, player.getPlayerId(), session).getBody();
    }

    @Override
    public List<PlayerSession> list(Player player) {
        MultiValueMap<String, String> header = new LinkedMultiValueMap<String, String>();
        header.add("playerId", String.valueOf(player.getPlayerId()));
        header.add("Content-Type", "application/json");
        // Step 1. Generating request
        HttpEntity<Void> requestEntity = new HttpEntity<Void>((Void) null, header);
        // Step 3. Rest template generation
        return restTemplate.exchange(baseUrl + PlayerWebMapping.PLAYER_PREFIX + PlayerWebMapping.PLAYER_SESSIONS, HttpMethod.GET, requestEntity,
                new ParameterizedTypeReference<List<PlayerSession>>() {
                }, player.getPlayerId()).getBody();
    }

}
