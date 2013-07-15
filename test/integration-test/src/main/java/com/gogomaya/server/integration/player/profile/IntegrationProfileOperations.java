package com.gogomaya.server.integration.player.profile;

import static com.google.common.base.Preconditions.checkNotNull;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.gogomaya.server.integration.player.Player;
import com.gogomaya.server.player.PlayerProfile;
import com.gogomaya.server.web.mapping.PlayerWebMapping;

public class IntegrationProfileOperations implements ProfileOperations {

    final private RestTemplate restTemplate;
    final private String baseUrl;

    public IntegrationProfileOperations(RestTemplate restTemplate, String baseUrl) {
        this.restTemplate = checkNotNull(restTemplate);
        this.baseUrl = checkNotNull(baseUrl);
    }

    @Override
    public PlayerProfile get(Player player, long playerId) {
        // Step 1. Creating Header
        MultiValueMap<String, String> header = new LinkedMultiValueMap<String, String>();
        header.add("playerId", String.valueOf(player.getPlayerId()));
        header.add("Content-Type", "application/json");
        // Step 2. Generating request
        HttpEntity<Void> requestEntity = new HttpEntity<Void>(null, header);
        // Step 3. Rest template generation
        return restTemplate.exchange(baseUrl + PlayerWebMapping.PLAYER_PREFIX + PlayerWebMapping.PLAYER_PROFILE, HttpMethod.GET, requestEntity,
                PlayerProfile.class, playerId).getBody();
    }

    @Override
    public PlayerProfile put(Player player, long playerId, PlayerProfile newProfile) {
        // Step 1. Creating Header
        MultiValueMap<String, String> header = new LinkedMultiValueMap<String, String>();
        header.add("playerId", String.valueOf(player.getPlayerId()));
        header.add("Content-Type", "application/json");
        // Step 2. Generating request
        HttpEntity<PlayerProfile> requestEntity = new HttpEntity<PlayerProfile>(newProfile, header);
        // Step 3. Rest template generation
        return restTemplate.exchange(baseUrl + PlayerWebMapping.PLAYER_PREFIX + PlayerWebMapping.PLAYER_PROFILE, HttpMethod.PUT, requestEntity,
                PlayerProfile.class, playerId).getBody();
    }

}
