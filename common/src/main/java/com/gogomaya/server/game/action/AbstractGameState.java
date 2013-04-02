package com.gogomaya.server.game.action;

import java.util.Collection;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.gogomaya.server.player.PlayerAwareUtils;

abstract public class AbstractGameState<M extends GameMove, S extends GamePlayerState> implements GameState<M, S> {

    /**
     * Generated 02/04/13
     */
    private static final long serialVersionUID = -6468020813755923981L;

    final private Map<Long, S> playersState;

    final private Map<Long, M> nextMoves;

    @JsonIgnore
    final private Map<Long, M> madeMoves;

    protected AbstractGameState(final Collection<S> playersStates, final Collection<M> nextMoves, final Collection<M> madeMoves) {
        this.playersState = PlayerAwareUtils.toMap(playersStates);
        this.nextMoves = PlayerAwareUtils.toMap(nextMoves);
        this.madeMoves = PlayerAwareUtils.toMap(madeMoves);
    }

    @Override
    final public Collection<S> getPlayerStates() {
        return playersState.values();
    }

    @Override
    final public Collection<M> getNextMoves() {
        return nextMoves.values();
    }

    @Override
    final public Collection<M> getMadeMoves() {
        return madeMoves.values();
    }

    @Override
    public S getPlayerState(long playerId) {
        return playersState.get(playerId);
    }

    @Override
    public M getNextMove(long playerId) {
        return nextMoves.get(playerId);
    }

    @Override
    public M getMadeMove(long playerId) {
        return madeMoves.get(playerId);
    }

}
