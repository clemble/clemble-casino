package com.gogomaya.server.integration.player.session;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;

import com.gogomaya.player.security.PlayerSession;
import com.gogomaya.server.integration.player.Player;

public class PlayerSessionOperations{
    
    final private SessionOperations sessionOperations;
    final private Player player;
    
    public PlayerSessionOperations(Player player, SessionOperations sessionOperations) {
        this.player = checkNotNull(player);
        this.sessionOperations = checkNotNull(sessionOperations);
    }

    public PlayerSession start() {
        return sessionOperations.start(player);
    }

    public PlayerSession end(long session) {
        return sessionOperations.end(player, session);
    }

    public PlayerSession refresh(long session) {
        return sessionOperations.refresh(player, session);
    }

    public PlayerSession get(long session) {
        return sessionOperations.get(player, session);
    }

    public List<PlayerSession> list() {
        return sessionOperations.list(player);
    }

}
