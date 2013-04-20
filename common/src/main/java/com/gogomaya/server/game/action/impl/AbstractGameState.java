package com.gogomaya.server.game.action.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

import com.gogomaya.server.game.action.GamePlayerIterator;
import com.gogomaya.server.game.action.GamePlayerState;
import com.gogomaya.server.game.action.GameState;
import com.gogomaya.server.game.action.move.GameMove;
import com.gogomaya.server.player.PlayerAwareUtils;

@JsonIgnoreProperties(value = { "activeUsers" })
abstract public class AbstractGameState<M extends GameMove, S extends GamePlayerState> implements GameState<M, S> {

    /**
     * Generated 02/04/13
     */
    private static final long serialVersionUID = -6468020813755923981L;

    private Map<Long, S> playersState;
    @JsonIgnore
    private GamePlayerIterator playerIterator;

    private Map<Long, M> nextMoves = new HashMap<Long, M>();
    @JsonIgnore
    private Map<Long, M> madeMoves = new HashMap<Long, M>();

    private int version;

    final public Collection<S> getPlayerStates() {
        return playersState.values();
    }

    final public GameState<M, S> setPlayerStates(Collection<S> playersStates) {
        playersState = PlayerAwareUtils.toMap(playersStates);
        return this;
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
    @JsonIgnore
    @JsonProperty("madeMoves")
    final public Collection<M> getMadeMoves() {
        return madeMoves.values();
    }

    @Override
    final public GameState<M, S> setMadeMoves(Collection<M> moves) {
        cleanMadeMove();
        for (M move : moves)
            madeMoves.put(move.getPlayerId(), move);
        return this;
    }

    @Override
    final public GameState<M, S> addMadeMove(M playerMove) {
        if (nextMoves.remove(playerMove.getPlayerId()) != null)
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

    @Override
    final public GameState<M, S> setPlayerIterator(GamePlayerIterator playerIterator) {
        this.playerIterator = playerIterator;
        return this;
    }

    final public int getVersion() {
        return version;
    }

    final public GameState<M, S> setVersion(int version) {
        this.version = version;
        return this;
    }

    final public GameState<M, S> incrementVersion() {
        this.version++;
        return this;
    }

}
