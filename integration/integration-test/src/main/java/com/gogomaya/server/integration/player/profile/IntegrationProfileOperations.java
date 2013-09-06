package com.gogomaya.server.integration.player.profile;

import static com.google.common.base.Preconditions.checkNotNull;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import com.gogomaya.configuration.ResourceLocations;
import com.gogomaya.player.PlayerProfile;
import com.gogomaya.server.integration.player.Player;
import com.gogomaya.web.player.PlayerWebMapping;

public class IntegrationProfileOperations implements ProfileOperations {

    final private RestTemplate restTemplate;

    public IntegrationProfileOperations(RestTemplate restTemplate) {
        this.restTemplate = checkNotNull(restTemplate);
    }

    @Override
    public PlayerProfile get(Player player, long playerId) {
        ResourceLocations resourceLocations = player.getSession().getResourceLocations();
        // Step 1. Creating signed request
        HttpEntity<Void> requestEntity = player.<Void>sign(null);
        // Step 3. Rest template generation
        return restTemplate.exchange(resourceLocations.getPlayerProfileEndpoint() + PlayerWebMapping.PLAYER_PROFILE, HttpMethod.GET, requestEntity,
                PlayerProfile.class, playerId).getBody();
    }

    @Override
    public PlayerProfile put(Player player, long playerId, PlayerProfile newProfile) {
        ResourceLocations resourceLocations = player.getSession().getResourceLocations();
        // Step 2. Generating request
        HttpEntity<PlayerProfile> requestEntity = player.<PlayerProfile>sign(newProfile);
        // Step 3. Rest template generation
        return restTemplate.exchange(resourceLocations.getPlayerProfileEndpoint() + PlayerWebMapping.PLAYER_PROFILE, HttpMethod.PUT, requestEntity, PlayerProfile.class, playerId).getBody();
    }

}
