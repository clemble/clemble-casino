package com.clemble.casino.android.player;

import org.springframework.web.client.RestTemplate;

import com.clemble.casino.ServerRegistry;
import com.clemble.casino.android.AbstractClembleCasinoOperations;
import com.clemble.casino.player.security.PlayerSession;
import com.clemble.casino.player.service.PlayerSessionService;
import com.clemble.casino.web.management.ManagementWebMapping;

public class AndroidPlayerSessionService extends AbstractClembleCasinoOperations implements PlayerSessionService {

    final private RestTemplate restClientService;

    public AndroidPlayerSessionService(RestTemplate restClientService, ServerRegistry serverRegistry) {
        super(serverRegistry);
        this.restClientService = restClientService;
    }

    @Override
    public PlayerSession create(String player) {
        return restClientService
            .postForEntity(buildUriById(player, ManagementWebMapping.MANAGEMENT_PLAYER_SESSIONS, "player", player), null, PlayerSession.class)
            .getBody();
    }

    @Override
    public PlayerSession refreshPlayerSession(String player, long sessionId) {
        return restClientService
            .postForEntity(buildUriById(player, ManagementWebMapping.MANAGEMENT_PLAYER_SESSIONS_SESSION, player, String.valueOf(sessionId)), null, PlayerSession.class)
            .getBody();
    }

    @Override
    public void endPlayerSession(String player, long sessionId) {
        restClientService
            .delete(buildUriById(player, ManagementWebMapping.MANAGEMENT_PLAYER_SESSIONS_SESSION, player, String.valueOf(sessionId)));
    }

    @Override
    public PlayerSession getPlayerSession(String player, long sessionId) {
        return restClientService
            .getForEntity(buildUriById(player, ManagementWebMapping.MANAGEMENT_PLAYER_SESSIONS_SESSION, player, String.valueOf(sessionId)), PlayerSession.class)
            .getBody();
    }

}
