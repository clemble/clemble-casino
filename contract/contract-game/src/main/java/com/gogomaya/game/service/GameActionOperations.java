package com.gogomaya.game.service;

import com.gogomaya.event.ClientEvent;
import com.gogomaya.game.GameState;
import com.gogomaya.game.SessionAware;
import com.gogomaya.game.event.client.MadeMove;
import com.gogomaya.player.PlayerAware;

public interface GameActionOperations<State extends GameState> extends SessionAware, PlayerAware {

    public State process(ClientEvent move);

    public MadeMove getAction(int actionId);

}
