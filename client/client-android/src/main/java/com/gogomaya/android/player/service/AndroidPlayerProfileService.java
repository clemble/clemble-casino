package com.gogomaya.android.player.service;

import static com.gogomaya.utils.Preconditions.checkNotNull;

import javax.inject.Inject;

import org.springframework.http.HttpEntity;

import com.gogomaya.player.PlayerProfile;
import com.gogomaya.player.service.PlayerProfileService;
import com.gogomaya.player.service.PlayerSecurityService;
import com.gogomaya.player.service.RESTService;
import com.gogomaya.web.player.PlayerWebMapping;

public class AndroidPlayerProfileService implements PlayerProfileService {

    final private RESTService restService;

    @Inject
    public AndroidPlayerProfileService(RESTService restService, PlayerSecurityService<HttpEntity<?>> securityService) {
        this.restService = checkNotNull(restService);
    }

    @Override
    public PlayerProfile getPlayerProfile(long playerId) {
        return restService.getForEntity(PlayerWebMapping.PLAYER_PREFIX, PlayerWebMapping.PLAYER_PROFILE, PlayerProfile.class, playerId);
    }

    @Override
    public PlayerProfile updatePlayerProfile(long playerId, PlayerProfile playerProfile) {
        return restService.postForEntity(PlayerWebMapping.PLAYER_PREFIX, PlayerWebMapping.PLAYER_PROFILE, playerProfile, PlayerProfile.class, playerId);
    }

}
