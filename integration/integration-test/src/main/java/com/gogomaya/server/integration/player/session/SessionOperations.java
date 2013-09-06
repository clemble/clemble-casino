package com.gogomaya.server.integration.player.session;

import com.gogomaya.player.security.PlayerSession;
import com.gogomaya.server.integration.player.Player;

public interface SessionOperations {

    public PlayerSession start(Player player);

    public PlayerSession end(Player player, PlayerSession session);

    public PlayerSession end(Player player, long session);

    public PlayerSession refresh(Player player, PlayerSession session);

    public PlayerSession refresh(Player player, long session);

    public PlayerSession get(Player player, long session);
}
