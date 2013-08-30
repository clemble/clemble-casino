package com.gogomaya.server.game.event.server;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.gogomaya.game.SessionAware;
import com.gogomaya.server.game.GameState;
import com.gogomaya.server.game.GameTable;
import com.gogomaya.server.game.ServerResourse;

@JsonTypeName("started")
public class GameStartedEvent<State extends GameState> extends GameServerEvent<State> implements SessionAware {

    /**
     * Generated
     */
    private static final long serialVersionUID = -4474960027054354888L;

    private long session;

    private ServerResourse resource;

    public GameStartedEvent() {
    }

    public GameStartedEvent(long session, GameTable<State> table) {
        super(table.getCurrentSession());
        this.session = session;
        this.resource = table.fetchServerResourse();
    }

    public GameStartedEvent(SessionAware sessionAware) {
        super(sessionAware);
    }

    public GameStartedEvent(SessionAware sessionAware, State state) {
        super(sessionAware);
        this.setState(state);
    }

    public ServerResourse getResource() {
        return resource;
    }

    public void setResource(ServerResourse resource) {
        this.resource = resource;
    }

    @Override
    public long getSession() {
        return session;
    }

    public GameServerEvent<State> setSession(long construction) {
        this.session = construction;
        return this;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((resource == null) ? 0 : resource.hashCode());
        result = prime * result + (int) (session ^ (session >>> 32));
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
        GameStartedEvent other = (GameStartedEvent) obj;
        if (resource == null) {
            if (other.resource != null)
                return false;
        } else if (!resource.equals(other.resource))
            return false;
        if (session != other.session)
            return false;
        return true;
    }

}
