package com.clemble.casino.android.player;

import static com.clemble.casino.utils.Preconditions.checkNotNull;

import com.clemble.casino.client.player.PlayerSessionOperations;
import com.clemble.casino.player.security.PlayerSession;
import com.clemble.casino.player.service.PlayerSessionService;

public class SimplePlayerSessionOperations implements PlayerSessionOperations {
    
    final private String player;
    final private PlayerSessionService playerSessionService;
    
    public SimplePlayerSessionOperations(String player, PlayerSessionService playerSessionService) {
        this.player = player;
        this.playerSessionService = checkNotNull(playerSessionService);
    }

    @Override
    public PlayerSession create() {
        return playerSessionService.create(player);
    }

    @Override
    public PlayerSession refreshPlayerSession(long sessionId) {
        return playerSessionService.refreshPlayerSession(player, sessionId);
    }

    @Override
    public PlayerSession endPlayerSession(long sessionId) {
        return playerSessionService.endPlayerSession(player, sessionId);
    }

    @Override
    public PlayerSession getPlayerSession(long sessionId) {
        return playerSessionService.getPlayerSession(player, sessionId);
    }

}
