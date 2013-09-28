package com.gogomaya.android.player.service;

import com.gogomaya.client.service.RestClientService;
import com.gogomaya.player.security.PlayerSession;
import com.gogomaya.player.service.PlayerSessionService;
import com.gogomaya.web.management.ManagementWebMapping;

public class AndroidPlayerSessionService implements PlayerSessionService {

    final private RestClientService restClientService;

    public AndroidPlayerSessionService(RestClientService restClientService) {
        this.restClientService = restClientService;
    }

    @Override
    public PlayerSession create(String player) {
        return restClientService.postForEntity(ManagementWebMapping.MANAGEMENT_PLAYER_SESSIONS, null, PlayerSession.class, player);
    }

    @Override
    public PlayerSession refreshPlayerSession(String player, long sessionId) {
        return restClientService.putForEntity(ManagementWebMapping.MANAGEMENT_PLAYER_SESSIONS_SESSION, null, PlayerSession.class, player, sessionId);
    }

    @Override
    public PlayerSession endPlayerSession(String player, long sessionId) {
        return restClientService.deleteForEntity(ManagementWebMapping.MANAGEMENT_PLAYER_SESSIONS_SESSION, null, PlayerSession.class, player, sessionId);
    }

    @Override
    public PlayerSession getPlayerSession(String player, long sessionId) {
        return restClientService.getForEntity(ManagementWebMapping.MANAGEMENT_PLAYER_SESSIONS_SESSION, null, PlayerSession.class, player, sessionId);
    }

}
