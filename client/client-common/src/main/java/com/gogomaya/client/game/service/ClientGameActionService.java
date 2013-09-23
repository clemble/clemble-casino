package com.gogomaya.client.game.service;

import com.gogomaya.game.GameState;
import com.gogomaya.game.service.GameActionService;

public interface ClientGameActionService<State extends GameState> extends GameActionService<State> {

    public ClientGameActionService<State> clone(String server);

}
