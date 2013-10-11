package com.clemble.casino.client.game.service;

import com.clemble.casino.event.ClientEvent;
import com.clemble.casino.event.listener.EventListener;
import com.clemble.casino.game.GameState;
import com.clemble.casino.game.SessionAware;
import com.clemble.casino.game.event.client.MadeMove;
import com.clemble.casino.player.PlayerAware;

public interface GameActionOperations<State extends GameState> extends SessionAware, PlayerAware {

    public State process(ClientEvent move);

    public MadeMove getAction(int actionId);

    public void subscribe(EventListener eventListener);

}
