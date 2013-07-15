package com.gogomaya.server.integration.player.session;

import java.util.List;

import com.gogomaya.server.integration.player.Player;
import com.gogomaya.server.player.security.PlayerSession;

public interface SessionOperations {

    public PlayerSession start(Player player);

    public PlayerSession end(Player player, PlayerSession session);

    public PlayerSession end(Player player, long session);

    public PlayerSession refresh(Player player, PlayerSession session);

    public PlayerSession refresh(Player player, long session);

    public PlayerSession get(Player player, long session);

    public List<PlayerSession> list(Player player);
}
