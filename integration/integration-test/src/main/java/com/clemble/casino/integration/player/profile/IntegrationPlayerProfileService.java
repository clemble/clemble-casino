package com.clemble.casino.integration.player.profile;

import static com.google.common.base.Preconditions.checkNotNull;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import com.clemble.casino.configuration.ResourceLocations;
import com.clemble.casino.integration.player.Player;
import com.clemble.casino.player.PlayerProfile;
import com.clemble.casino.player.service.PlayerProfileService;
import com.clemble.casino.web.player.PlayerWebMapping;

public class IntegrationPlayerProfileService implements PlayerProfileService {

    final private Player player;
    final private RestTemplate restTemplate;

    public IntegrationPlayerProfileService(Player player, RestTemplate restTemplate) {
        this.restTemplate = checkNotNull(restTemplate);
        this.player = checkNotNull(player);
    }

    @Override
    public PlayerProfile getPlayerProfile(String playerId) {
        ResourceLocations resourceLocations = player.getSession().getResourceLocations();
        // Step 1. Rest template generation
        return restTemplate.exchange(resourceLocations.getServerRegistryConfiguration().getPlayerRegistry().findById(playerId) + PlayerWebMapping.PLAYER_PROFILE, HttpMethod.GET, null,
                PlayerProfile.class, playerId).getBody();
    }

    @Override
    public PlayerProfile updatePlayerProfile(String playerId, PlayerProfile newProfile) {
        ResourceLocations resourceLocations = player.getSession().getResourceLocations();
        // Step 2. Generating request
        HttpEntity<PlayerProfile> requestEntity = new HttpEntity<>(newProfile);
        // Step 3. Rest template generation
        return restTemplate.exchange(resourceLocations.getServerRegistryConfiguration().getPlayerRegistry().findById(newProfile.getPlayer()) + PlayerWebMapping.PLAYER_PROFILE, HttpMethod.PUT, requestEntity, PlayerProfile.class, playerId).getBody();
    }

}
