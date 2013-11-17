package com.clemble.casino.client.game;

import com.clemble.casino.game.GameSessionKey;
import com.clemble.casino.game.GameState;

public interface GameActionOperationsFactory {

    public <State extends GameState> GameActionOperations<State> construct(GameSessionKey sessionKey);

}
