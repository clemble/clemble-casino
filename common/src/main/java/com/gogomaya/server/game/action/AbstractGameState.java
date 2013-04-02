package com.gogomaya.server.game.action;

import java.util.Collection;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.google.common.collect.ImmutableList;

abstract public class AbstractGameState<M extends GameMove, S extends GamePlayerState> implements GameState {

    /**
     * Generated 02/04/13
     */
    private static final long serialVersionUID = -6468020813755923981L;

    final private Collection<S> playersState;

    final private Collection<M> nextMoves;
    @JsonIgnore
    final private Collection<M> madeMoves;

    protected AbstractGameState(final Collection<S> playersStates,
            final Collection<M> nextMoves,
            final Collection<M> madeMoves) {
        this.playersState = ImmutableList.<S> copyOf(playersStates);
        this.nextMoves = ImmutableList.<M>copyOf(nextMoves);
        this.madeMoves = ImmutableList.<M>copyOf(madeMoves);
    }

    @Override
    final public Collection<S> getPlayersStates() {
        return playersState;
    }

    @Override
    final public Collection<M> getNextMoves() {
        return nextMoves;
    }

    @Override
    final public Collection<M> getMadeMoves() {
        return madeMoves;
    }

}
