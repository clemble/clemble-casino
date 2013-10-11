package com.clemble.casino.android.player.service;

import static com.clemble.casino.utils.Preconditions.checkNotNull;

import javax.inject.Inject;

import com.clemble.casino.player.PlayerProfile;
import com.clemble.casino.player.service.PlayerProfileService;
import com.clemble.casino.web.player.PlayerWebMapping;
import com.clemble.casino.client.service.RestClientService;

public class AndroidPlayerProfileService implements PlayerProfileService {

    final private RestClientService restService;

    @Inject
    public AndroidPlayerProfileService(RestClientService restService) {
        this.restService = checkNotNull(restService);
    }

    @Override
    public PlayerProfile getPlayerProfile(String player) {
        return restService.getForEntity(PlayerWebMapping.PLAYER_PROFILE, PlayerProfile.class, player);
    }

    @Override
    public PlayerProfile updatePlayerProfile(String player, PlayerProfile playerProfile) {
        return restService.putForEntity(PlayerWebMapping.PLAYER_PROFILE, playerProfile, PlayerProfile.class, player);
    }

}
