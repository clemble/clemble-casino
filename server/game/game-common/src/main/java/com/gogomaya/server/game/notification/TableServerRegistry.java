package com.gogomaya.server.game.notification;

import static com.google.common.base.Preconditions.checkNotNull;

import com.gogomaya.game.GameState;
import com.gogomaya.game.ServerResourse;
import com.gogomaya.game.SessionAware;
import com.gogomaya.server.ServerRegistry;

public class TableServerRegistry {

    final private ServerRegistry SERVER_REGISTRY;

    public TableServerRegistry(ServerRegistry serverRegistry) {
        this.SERVER_REGISTRY = checkNotNull(serverRegistry);
    }

    public String findServer(long session) {
        return SERVER_REGISTRY.find(session);
    }

    public <State extends GameState> ServerResourse findServer(SessionAware sessionAware) {
        if (sessionAware == null)
            return null;

        return new ServerResourse(findServer(sessionAware.getSession().getSession()), sessionAware.getSession());
    }

}
