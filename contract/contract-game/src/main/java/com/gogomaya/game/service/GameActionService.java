package com.gogomaya.game.service;

import com.gogomaya.event.ClientEvent;
import com.gogomaya.game.GameState;
import com.gogomaya.game.event.client.MadeMove;

public interface GameActionService<State extends GameState> {

    public State process(long sessionId, ClientEvent move);

    public MadeMove getAction(long sessionId, int actionId);

}
