package com.gogomaya.server.integration.player.session;

import com.gogomaya.server.integration.player.Player;
import com.gogomaya.server.player.security.PlayerSession;

public abstract class AbstractSessionOperations implements SessionOperations {

    final public PlayerSession end(Player player, PlayerSession session) {
        return end(player, session.getSessionId());
    }

    final public PlayerSession refresh(Player player, PlayerSession session) {
        return refresh(player, session.getSessionId());
    }

}
