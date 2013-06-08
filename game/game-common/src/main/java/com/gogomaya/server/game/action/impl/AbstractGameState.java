package com.gogomaya.server.game.action.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.gogomaya.server.error.GogomayaError;
import com.gogomaya.server.error.GogomayaException;
import com.gogomaya.server.game.action.GameOutcome;
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

    private Map<Long, GamePlayerState> playersState = new HashMap<Long, GamePlayerState>();
    @JsonIgnore
    private GamePlayerIterator playerIterator;

    private Map<Long, GameMove> nextMoves = new HashMap<Long, GameMove>();
    @JsonIgnore
    private Map<Long, GameMove> madeMoves = new HashMap<Long, GameMove>();

    private GameOutcome outcome;

    @JsonProperty("version")
    private int version;

    @Override
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

    final public Set<Long> getOpponents(long playerId) {
        // Step 1. Calculating opponents from the original list
        Set<Long> opponents = new HashSet<Long>();
        for (long opponent : playerIterator.getPlayers()) {
            if (opponent != playerId) {
                opponents.add(opponent);
            }
        }
        // Step 2. Returning list of opponents
        return opponents;
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
    final public int getVersion() {
        return version;
    }

    abstract public GameOutcome calculate();

    @Override
    public GameOutcome getOutcome() {
        // Step 1. If we already calculated outcome return it
        if (outcome != null)
            return outcome;
        // Step 2. Checking if there is a single winner
        this.outcome = calculate();
        // Step 3. Returning GameOutcome
        return outcome;
    }

    @Override
    public GameState setOutcome(GameOutcome outcome) {
        if (this.outcome != null)
            throw GogomayaException.create(GogomayaError.ServerCriticalError);
        this.outcome = outcome;
        this.version++;
        return this;
    }

    final public void setVersion(int version) {
        this.version = version;
    }

}
