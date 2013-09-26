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
    public PlayerSession create(long playerId) {
        return restClientService.postForEntity(ManagementWebMapping.MANAGEMENT_PLAYER_SESSIONS, null, PlayerSession.class, playerId);
    }

    @Override
    public PlayerSession refreshPlayerSession(long playerId, long sessionId) {
        return restClientService.putForEntity(ManagementWebMapping.MANAGEMENT_PLAYER_SESSIONS_SESSION, null, PlayerSession.class, playerId, sessionId);
    }

    @Override
    public PlayerSession endPlayerSession(long playerId, long sessionId) {
        return restClientService.deleteForEntity(ManagementWebMapping.MANAGEMENT_PLAYER_SESSIONS_SESSION, null, PlayerSession.class, playerId, sessionId);
    }

    @Override
    public PlayerSession getPlayerSession(long playerId, long sessionId) {
        return restClientService.getForEntity(ManagementWebMapping.MANAGEMENT_PLAYER_SESSIONS_SESSION, null, PlayerSession.class, playerId, sessionId);
    }

}
