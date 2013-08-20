package com.gogomaya.server.go;

import com.gogomaya.server.ActionLatch;
import com.gogomaya.server.game.GamePlayerIterator;
import com.gogomaya.server.game.GameAccount;
import com.gogomaya.server.game.GameState;
import com.gogomaya.server.game.outcome.GameOutcome;

public class GoGameState implements GameState {

    /**
     * 
     */
    private static final long serialVersionUID = -8322660522348015422L;

    final private GameAccount playerStateManager;

    public GoGameState(GameAccount playerStateManager) {
        this.playerStateManager = playerStateManager;
    }

    @Override
    public GameAccount getAccount() {
        return playerStateManager;
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
