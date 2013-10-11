package com.clemble.casino.game.service;

import com.clemble.casino.event.ClientEvent;
import com.clemble.casino.game.GameState;
import com.clemble.casino.game.event.client.MadeMove;

public interface GameActionService<State extends GameState> {

    public State process(long sessionId, ClientEvent move);

    public MadeMove getAction(long sessionId, int actionId);

}