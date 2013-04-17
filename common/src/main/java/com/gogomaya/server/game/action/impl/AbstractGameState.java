package com.gogomaya.server.game.action.impl;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

import com.gogomaya.server.game.action.GamePlayerIterator;
import com.gogomaya.server.game.action.GamePlayerState;
import com.gogomaya.server.game.action.GameState;
import com.gogomaya.server.game.action.move.GameMove;
import com.gogomaya.server.player.PlayerAwareUtils;

abstract public class AbstractGameState<M extends GameMove, S extends GamePlayerState> implements GameState<M, S> {

    /**
     * Generated 02/04/13
     */
    private static final long serialVersionUID = -6468020813755923981L;

    final private Map<Long, S> playersState;

    final private Map<Long, M> nextMoves = new HashMap<Long, M>();
    @JsonIgnore
    final private GamePlayerIterator playerIterator;
    @JsonIgnore
    final private Map<Long, M> madeMoves = new HashMap<Long, M>();

    protected AbstractGameState(final Collection<S> playersStates, final GamePlayerIterator playerIterator) {
        this.playersState = PlayerAwareUtils.toMap(playersStates);
        this.playerIterator = checkNotNull(playerIterator);
    }

    @Override
    final public S getPlayerState(long playerId) {
        return playersState.get(playerId);
    }

    @Override
    final public GameState<M, S> setPlayerState(S newPlayerState) {
        playersState.put(newPlayerState.getPlayerId(), newPlayerState);
        return this;
    }

    final protected void updatePlayerState(S playerState) {
        this.playersState.put(playerState.getPlayerId(), playerState);
    }

    @JsonIgnore
    final public Set<Long> getActiveUsers() {
        return nextMoves.keySet();
    }

    @Override
    @JsonProperty("nextMoves")
    final public Collection<M> getNextMoves() {
        return nextMoves.values();
    }

    @Override
    final public M getNextMove(long playerId) {
        return nextMoves.get(playerId);
    }

    @Override
    final public GameState<M, S> setNextMove(M move) {
        nextMoves.clear();
        nextMoves.put(move.getPlayerId(), move);
        return this;
    }

    @Override
    final public GameState<M, S> setNextMoves(Collection<M> moves) {
        nextMoves.clear();
        for (M move : moves)
            nextMoves.put(move.getPlayerId(), move);
        return this;
    }

    @Override
    final public M getMadeMove(long playerId) {
        return madeMoves.get(playerId);
    }

    @Override
    final public Collection<M> getMadeMoves() {
        return madeMoves.values();
    }


    @Override
    final public GameState<M, S> addMadeMove(M playerMove) {
        madeMoves.put(playerMove.getPlayerId(), playerMove);
        return this;
    }

    @Override
    final public GameState<M, S> cleanMadeMove() {
        madeMoves.clear();
        return this;
    }

    @Override
    final public GamePlayerIterator getPlayerIterator() {
        return playerIterator;
    }

}
