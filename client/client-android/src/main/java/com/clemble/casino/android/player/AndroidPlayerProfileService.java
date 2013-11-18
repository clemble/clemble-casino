package com.clemble.casino.android.player;

import static com.clemble.casino.utils.Preconditions.checkNotNull;

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
        return restTemplate
            .getForEntity(buildUriWith(PlayerWebMapping.PLAYER_PROFILE, player), PlayerProfile.class)
            .getBody();
    }

    @Override
    public PlayerProfile updatePlayerProfile(String player, PlayerProfile playerProfile) {
        return restTemplate
            .postForEntity(buildUriWith(PlayerWebMapping.PLAYER_PROFILE, player), playerProfile, PlayerProfile.class)
            .getBody();
    }

}
