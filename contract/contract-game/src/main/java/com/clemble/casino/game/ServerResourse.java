package com.clemble.casino.game;

import com.clemble.casino.game.GameSessionKey;
import com.clemble.casino.game.SessionAware;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ServerResourse implements SessionAware {

    /**
     * 
     */
    private static final long serialVersionUID = 2462996262286612159L;

    final private String server;
    final private GameSessionKey session;

    @JsonCreator
    public ServerResourse(@JsonProperty("server") final String server, @JsonProperty("session") final GameSessionKey session) {
        this.server = server;
        this.session = session;
    }

    public String getServer() {
        return server;
    }

    @Override
    public GameSessionKey getSession() {
        return session;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((server == null) ? 0 : server.hashCode());
        result = prime * result + ((session == null) ? 0 : session.hashCode());;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ServerResourse other = (ServerResourse) obj;
        if (server == null) {
            if (other.server != null)
                return false;
        } else if (!server.equals(other.server))
            return false;
        return session.equals(other.session);
    }

}
