package com.clemble.casino.integration.game;

import com.clemble.casino.event.Event;
import com.clemble.casino.game.lifecycle.management.GameState;
import com.clemble.casino.game.lifecycle.management.RoundGameState;
import com.clemble.casino.game.lifecycle.management.RoundState;
import com.clemble.casino.lifecycle.management.event.action.Action;

public interface RoundGamePlayer extends GamePlayer {

    public RoundGameState getState();

    public boolean isToMove();

    public Event getNextMove();

    public void waitForTurn();

    public void perform(Action gameAction);

}
