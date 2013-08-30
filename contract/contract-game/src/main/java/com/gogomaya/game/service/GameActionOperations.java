package com.gogomaya.game.service;

import com.gogomaya.event.ClientEvent;
import com.gogomaya.game.GameState;
import com.gogomaya.game.event.client.MadeMove;

public interface GameActionOperations<State extends GameState> {

    public State process(ClientEvent move);

    public MadeMove getAction(int actionId);

}
