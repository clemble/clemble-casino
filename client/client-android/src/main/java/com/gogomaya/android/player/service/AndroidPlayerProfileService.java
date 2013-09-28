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
    public PlayerProfile getPlayerProfile(String player) {
        return restService.getForEntity(PlayerWebMapping.PLAYER_PROFILE, PlayerProfile.class, player);
    }

    @Override
    public PlayerProfile updatePlayerProfile(String player, PlayerProfile playerProfile) {
        return restService.putForEntity(PlayerWebMapping.PLAYER_PROFILE, playerProfile, PlayerProfile.class, player);
    }

}
