package com.clemble.casino.game.service;

import com.clemble.casino.event.ClientEvent;
import com.clemble.casino.game.Game;
import com.clemble.casino.game.GameState;
import com.clemble.casino.game.event.client.MadeMove;

public interface GameActionService<State extends GameState> {

    public State getState(Game game, String session);

    public State process(Game game, String sessionId, ClientEvent move);

    public MadeMove getAction(Game game, String sessionId, int actionId);

}
