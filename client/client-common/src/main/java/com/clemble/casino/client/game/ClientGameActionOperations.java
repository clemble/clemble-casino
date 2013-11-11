package com.clemble.casino.client.game;

import com.clemble.casino.game.GameState;
import com.clemble.casino.game.service.GameActionService;

public interface ClientGameActionOperations<State extends GameState> extends GameActionService<State> {

}
