package com.clemble.casino.integration.player.session;

import com.clemble.casino.integration.player.Player;
import com.clemble.casino.player.security.PlayerSession;

public abstract class AbstractSessionOperations implements SessionOperations {

    final public void end(Player player, PlayerSession session) {
        end(player, session.getSessionId());
    }

    final public PlayerSession refresh(Player player, PlayerSession session) {
        return refresh(player, session.getSessionId());
    }

}
