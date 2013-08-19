package com.gogomaya.server.go;

import java.util.Collection;

import com.gogomaya.server.ActionLatch;
import com.gogomaya.server.game.GamePlayerIterator;
import com.gogomaya.server.game.GamePlayerState;
import com.gogomaya.server.game.GameState;
import com.gogomaya.server.game.outcome.GameOutcome;

public class GoGameState implements GameState{

    @Override
    public Collection<GamePlayerState> getPlayerStates() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public GamePlayerState getPlayerState(long playerId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public GameState setPlayerState(GamePlayerState player) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public GameState setPlayerStates(Collection<GamePlayerState> playersStates) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public GamePlayerIterator getPlayerIterator() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public GameState setPlayerIterator(GamePlayerIterator playerIterator) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public GameOutcome getOutcome() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public GameState setOutcome(GameOutcome outcome) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean complete() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public ActionLatch getActionLatch() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int getVersion() {
        // TODO Auto-generated method stub
        return 0;
    }

}
