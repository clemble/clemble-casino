package com.gogomaya.server.game.event.server;

import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.gogomaya.server.event.ClientEvent;
import com.gogomaya.server.game.GameState;
import com.gogomaya.server.player.PlayerAware;

@JsonTypeName("playerMoved")
public class PlayerMovedEvent<State extends GameState> extends GameServerEvent<State> implements PlayerAware {

    /**
     * Generated 07/05/2013
     */
    private static final long serialVersionUID = -3272848407005579296L;

    private ClientEvent madeMove;

    private Collection<ClientEvent> nextMoves;

    public PlayerMovedEvent() {
    }

    public PlayerMovedEvent(State state) {
        this.setState(state);
    }

    public ClientEvent getMadeMove() {
        return madeMove;
    }

    public PlayerMovedEvent<State> setMadeMove(ClientEvent madeMove) {
        this.madeMove = madeMove;
        return this;
    }

    public Collection<ClientEvent> getNextMoves() {
        return nextMoves;
    }

    public PlayerMovedEvent<State> setNextMoves(Collection<ClientEvent> nextMoves) {
        this.nextMoves = nextMoves;
        return this;
    }

    @Override
    @JsonIgnore
    public long getPlayerId() {
        return madeMove.getPlayerId();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((madeMove == null) ? 0 : madeMove.hashCode());
        result = prime * result + ((nextMoves == null) ? 0 : nextMoves.hashCode());
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
        PlayerMovedEvent other = (PlayerMovedEvent) obj;
        if (madeMove == null) {
            if (other.madeMove != null)
                return false;
        } else if (!madeMove.equals(other.madeMove))
            return false;
        if (nextMoves == null) {
            if (other.nextMoves != null)
                return false;
        } else if (!nextMoves.equals(other.nextMoves))
            return false;
        return true;
    }

}
