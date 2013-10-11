package com.clemble.casino.server.game.notification;

import static com.google.common.base.Preconditions.checkNotNull;

import com.clemble.casino.game.GameState;
import com.clemble.casino.game.ServerResourse;
import com.clemble.casino.game.SessionAware;
import com.clemble.casino.server.LongServerRegistry;

public class TableServerRegistry {

    final private LongServerRegistry SERVER_REGISTRY;

    public TableServerRegistry(LongServerRegistry serverRegistry) {
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
