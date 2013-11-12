package com.clemble.casino.integration.player.session;

import com.clemble.casino.integration.player.Player;
import com.clemble.casino.player.security.PlayerSession;

public interface SessionOperations {

    public PlayerSession start(Player player);

    public void end(Player player, PlayerSession session);

    public void end(Player player, long session);

    public PlayerSession refresh(Player player, PlayerSession session);

    public PlayerSession refresh(Player player, long session);

    public PlayerSession get(Player player, long session);
}
