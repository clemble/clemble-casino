package com.clemble.casino.client.game;

import com.clemble.casino.client.event.EventListener;
import com.clemble.casino.event.ClientEvent;
import com.clemble.casino.game.GameState;
import com.clemble.casino.game.GameSessionAware;
import com.clemble.casino.game.event.client.MadeMove;
import com.clemble.casino.player.PlayerAware;

public interface GameActionOperations<State extends GameState> extends GameSessionAware, PlayerAware {

    public State getState();

    public State process(ClientEvent move);

    public MadeMove getAction(int actionId);

    public void subscribe(EventListener eventListener);

}
