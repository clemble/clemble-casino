package com.gogomaya.server.game.action;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonIgnore;

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

    protected AbstractGameState(final Collection<S> playersStates) {
        this.playersState = PlayerAwareUtils.toMap(playersStates);
        this.playerIterator = new GamePlayerIterator(0, playersStates);
    }

    @Override
    final public S getPlayerState(long playerId) {
        return playersState.get(playerId);
    }

    @Override
    final public void setPlayerState(S newPlayerState) {
        playersState.put(newPlayerState.getPlayerId(), newPlayerState);
    }

    final protected void updatePlayerState(S playerState) {
        this.playersState.put(playerState.getPlayerId(), playerState);
    }

    @Override
    final public M getNextMove(long playerId) {
        return nextMoves.get(playerId);
    }

    @Override
    final public void setNextMove(M move) {
        nextMoves.clear();
        nextMoves.put(move.getPlayerId(), move);
    }

    @Override
    final public void setNextMoves(Collection<M> moves) {
        nextMoves.clear();
        for (M move : moves)
            nextMoves.put(move.getPlayerId(), move);
    }

    @Override
    final public M getMadeMove(long playerId) {
        return madeMoves.get(playerId);
    }

    @Override
    final public void addMadeMove(M playerMove) {
        madeMoves.put(playerMove.getPlayerId(), playerMove);
    }

    @Override
    final public void cleanMadeMove() {
        madeMoves.clear();
    }

    @Override
    final public GamePlayerIterator getPlayerIterator() {
        return playerIterator;
    }

}
