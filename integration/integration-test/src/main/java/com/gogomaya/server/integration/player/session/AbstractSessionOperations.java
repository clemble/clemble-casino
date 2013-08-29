package com.gogomaya.server.integration.player.session;

import com.gogomaya.player.security.PlayerSession;
import com.gogomaya.server.integration.player.Player;

public abstract class AbstractSessionOperations implements SessionOperations {

    final public PlayerSession end(Player player, PlayerSession session) {
        return end(player, session.getSessionId());
    }

    final public PlayerSession refresh(Player player, PlayerSession session) {
        return refresh(player, session.getSessionId());
    }

}
