package com.clemble.casino.integration.game;

import com.clemble.casino.event.Event;
import com.clemble.casino.game.GameState;
import com.clemble.casino.game.action.GameAction;

public interface MatchGamePlayer<State extends GameState> extends GamePlayer {

    public State getState();

    public boolean isToMove();

    public Event getNextMove();

    public void waitForTurn();

    public void perform(GameAction gameAction);

}
