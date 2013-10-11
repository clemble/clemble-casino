package com.clemble.casino.integration.player.session;

import com.clemble.casino.integration.player.Player;
import com.clemble.casino.player.security.PlayerSession;

public abstract class AbstractSessionOperations implements SessionOperations {

    final public PlayerSession end(Player player, PlayerSession session) {
        return end(player, session.getSessionId());
    }

    final public PlayerSession refresh(Player player, PlayerSession session) {
        return refresh(player, session.getSessionId());
    }

}
