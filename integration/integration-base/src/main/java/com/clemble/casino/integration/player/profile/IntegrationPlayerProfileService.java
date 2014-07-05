package com.clemble.casino.integration.player.profile;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;
import java.util.List;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import com.clemble.casino.ServerRegistry;
import com.clemble.casino.player.PlayerProfile;
import com.clemble.casino.player.service.PlayerProfileService;
import static com.clemble.casino.web.player.PlayerWebMapping.*;

public class IntegrationPlayerProfileService implements PlayerProfileService {

    final private ServerRegistry playerRegistry;
    final private RestTemplate restTemplate;

    public IntegrationPlayerProfileService(ServerRegistry player, RestTemplate restTemplate) {
        this.restTemplate = checkNotNull(restTemplate);
        this.playerRegistry = player;
    }

    @Override
    public PlayerProfile getPlayerProfile(String player) {
        // Step 1. Rest template generation
        return restTemplate.exchange(playerRegistry.findById(player) + PROFILE_PREFIX + PROFILE_PLAYER, HttpMethod.GET, null, PlayerProfile.class, player).getBody();
    }

    @Override
    public PlayerProfile updatePlayerProfile(String player, PlayerProfile newProfile) {
        // Step 1. Generating request
        HttpEntity<PlayerProfile> requestEntity = new HttpEntity<>(newProfile);
        // Step 2. Rest template generation
        return restTemplate.exchange(playerRegistry.findById(newProfile.getPlayer()) + PROFILE_PREFIX + PROFILE_PLAYER, HttpMethod.PUT, requestEntity, PlayerProfile.class, player).getBody();
    }

    @Override
    public List<PlayerProfile> getPlayerProfile(Collection<String> players) {
        throw new UnsupportedOperationException();
    }

}
