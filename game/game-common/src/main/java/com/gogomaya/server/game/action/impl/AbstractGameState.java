package com.gogomaya.server.game.action.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

import com.gogomaya.server.error.GogomayaError;
import com.gogomaya.server.error.GogomayaException;
import com.gogomaya.server.game.action.GamePlayerIterator;
import com.gogomaya.server.game.action.GamePlayerState;
import com.gogomaya.server.game.action.GameState;
import com.gogomaya.server.game.action.move.GameMove;
import com.gogomaya.server.player.PlayerAwareUtils;

@JsonIgnoreProperties(value = { "activeUsers" })
abstract public class AbstractGameState implements GameState {

    /**
     * Generated 02/04/13
     */
    private static final long serialVersionUID = -6468020813755923981L;

    private Map<Long, GamePlayerState> playersState;
    @JsonIgnore
    private GamePlayerIterator playerIterator;

    private Map<Long, GameMove> nextMoves = new HashMap<Long, GameMove>();
    @JsonIgnore
    private Map<Long, GameMove> madeMoves = new HashMap<Long, GameMove>();

    @JsonProperty("version")
    private int version;

    final public Collection<GamePlayerState> getPlayerStates() {
        return playersState.values();
    }

    final public GameState setPlayerStates(Collection<GamePlayerState> playersStates) {
        playersState = PlayerAwareUtils.toMap(playersStates);
        return this;
    }

    @Override
    final public GamePlayerState getPlayerState(long playerId) {
        return playersState.get(playerId);
    }

    @Override
    final public GameState setPlayerState(GamePlayerState newPlayerState) {
        playersState.put(newPlayerState.getPlayerId(), newPlayerState);
        return this;
    }

    final protected void updatePlayerState(GamePlayerState playerState) {
        this.playersState.put(playerState.getPlayerId(), playerState);
    }

    @JsonIgnore
    final public Set<Long> getActiveUsers() {
        return nextMoves.keySet();
    }

    @Override
    @JsonProperty("nextMoves")
    final public Collection<GameMove> getNextMoves() {
        return nextMoves.values();
    }

    @Override
    final public GameMove getNextMove(long playerId) {
        return nextMoves.get(playerId);
    }

    final public GameState setNextMove(GameMove move) {
        nextMoves.clear();
        nextMoves.put(move.getPlayerId(), move);
        version++;
        return this;
    }

    final public GameState setNextMoves(Collection<GameMove> moves) {
        nextMoves.clear();
        for (GameMove move : moves)
            nextMoves.put(move.getPlayerId(), move);
        version++;
        return this;
    }

    final public GameMove getMadeMove(long playerId) {
        return madeMoves.get(playerId);
    }

    final public void setMadeMoves(Collection<GameMove> gameMoves) {
        madeMoves.clear();
        for (GameMove move : gameMoves) {
            madeMoves.put(move.getPlayerId(), move);
        }
        version++;
    }

    @Override
    @JsonIgnore
    @JsonProperty("madeMoves")
    final public Collection<GameMove> getMadeMoves() {
        return madeMoves.values();
    }

    final public GameState addMadeMove(GameMove playerMove) {
        if (nextMoves.remove(playerMove.getPlayerId()) != null) {
            madeMoves.put(playerMove.getPlayerId(), playerMove);
            version++;
        }
        return this;
    }

    final public GameState cleanMadeMove() {
        madeMoves.clear();
        return this;
    }

    @Override
    final public GamePlayerIterator getPlayerIterator() {
        return playerIterator;
    }

    @Override
    final public GameState setPlayerIterator(GamePlayerIterator playerIterator) {
        this.playerIterator = playerIterator;
        return this;
    }

    @Override
    public GameState process(final GameMove move) {
        // Step 0. Sanity check
        if (move == null)
            throw GogomayaException.create(GogomayaError.GamePlayMoveUndefined);
        final long playerId = move.getPlayerId();
        // Step 1.1. Checking that move
        GameMove associatedPlayerMove = madeMoves.get(playerId);
        if (associatedPlayerMove != null)
            throw GogomayaException.create(GogomayaError.GamePlayMoveAlreadyMade);
        GameMove expectedMove = nextMoves.get(playerId);
        if (expectedMove == null)
            throw GogomayaException.create(GogomayaError.GamePlayNoMoveExpected);
        if (expectedMove.getClass() != move.getClass())
            throw GogomayaException.create(GogomayaError.GamePlayWrongMoveType);
        // Step 2. Processing Select cell move
        return apply(move);
    }

    abstract protected GameState apply(GameMove move);

    final public int getVersion() {
        return version;
    }

    final public void setVersion(int version) {
        this.version = version;
    }

}
