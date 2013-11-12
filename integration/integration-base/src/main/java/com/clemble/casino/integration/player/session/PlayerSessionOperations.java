package com.clemble.casino.integration.player.session;

import static com.clemble.casino.utils.Preconditions.checkNotNull;

import com.clemble.casino.integration.player.Player;
import com.clemble.casino.player.security.PlayerSession;

public class PlayerSessionOperations {

    final private SessionOperations sessionOperations;
    final private Player player;

    public PlayerSessionOperations(Player player, SessionOperations sessionOperations) {
        this.player = checkNotNull(player);
        this.sessionOperations = checkNotNull(sessionOperations);
    }

    public PlayerSession start() {
        return sessionOperations.start(player);
    }

    public void end(long session) {
        sessionOperations.end(player, session);
    }

    public PlayerSession refresh(long session) {
        return sessionOperations.refresh(player, session);
    }

}
