package com.clemble.casino.client.game.service;

import com.clemble.casino.game.GameState;
import com.clemble.casino.game.service.GameActionService;

public interface ClientGameActionService<State extends GameState> extends GameActionService<State> {

    public ClientGameActionService<State> clone(String server);

}
