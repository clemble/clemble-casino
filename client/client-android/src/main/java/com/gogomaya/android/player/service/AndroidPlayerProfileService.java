package com.gogomaya.android.player.service;

import static com.gogomaya.utils.Preconditions.checkNotNull;

import javax.inject.Inject;

import com.gogomaya.client.service.RestClientService;
import com.gogomaya.player.PlayerProfile;
import com.gogomaya.player.service.PlayerProfileService;
import com.gogomaya.web.player.PlayerWebMapping;

public class AndroidPlayerProfileService implements PlayerProfileService {

    final private RestClientService restService;

    @Inject
    public AndroidPlayerProfileService(RestClientService restService) {
        this.restService = checkNotNull(restService);
    }

    @Override
    public PlayerProfile getPlayerProfile(long playerId) {
        return restService.getForEntity(PlayerWebMapping.PLAYER_PREFIX, PlayerWebMapping.PLAYER_PROFILE, PlayerProfile.class, playerId);
    }

    @Override
    public PlayerProfile updatePlayerProfile(long playerId, PlayerProfile playerProfile) {
        return restService.putForEntity(PlayerWebMapping.PLAYER_PREFIX, PlayerWebMapping.PLAYER_PROFILE, playerProfile, PlayerProfile.class, playerId);
    }

}
