package com.clemble.casino.android.player;

import static com.clemble.casino.utils.Preconditions.checkNotNull;

import java.net.URI;

import org.springframework.web.client.RestTemplate;

import com.clemble.casino.ServerRegistry;
import com.clemble.casino.android.AbstractClembleCasinoOperations;
import com.clemble.casino.player.PlayerProfile;
import com.clemble.casino.player.service.PlayerProfileService;
import com.clemble.casino.web.player.PlayerWebMapping;

public class AndroidPlayerProfileService extends AbstractClembleCasinoOperations implements PlayerProfileService {

    final private RestTemplate restTemplate;

    public AndroidPlayerProfileService(RestTemplate restService, ServerRegistry serverRegistry) {
        super(serverRegistry);
        this.restTemplate = checkNotNull(restService);
    }

    @Override
    public PlayerProfile getPlayerProfile(String player) {
        // Step 1. Generating player Uri
        URI playerUri = buildUriWith(PlayerWebMapping.PLAYER_PROFILE, player);
        // Step 2. Sending PlayerProfile request 
        return restTemplate.getForEntity(playerUri, PlayerProfile.class).getBody();
    }

    @Override
    public PlayerProfile updatePlayerProfile(String player, PlayerProfile playerProfile) {
        // Step 1. Generating player URI
        URI playerUri = buildUriWith(PlayerWebMapping.PLAYER_PROFILE, player);
        // Step 2. Post to Player URI
        return restTemplate.postForEntity(playerUri, playerProfile, PlayerProfile.class).getBody();
    }

}
