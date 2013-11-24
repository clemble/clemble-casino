package com.clemble.casino.integration.game;

import com.clemble.casino.client.ClembleCasinoOperations;
import com.clemble.casino.game.GameAware;
import com.clemble.casino.game.GameSessionKey;
import com.clemble.casino.game.GameState;
import com.clemble.casino.game.construct.GameConstruction;

public interface GameSessionPlayerFactory<State extends GameState> extends GameAware {

    public GameSessionPlayer<State> construct(ClembleCasinoOperations player, GameConstruction construction);

    public GameSessionPlayer<State> construct(ClembleCasinoOperations player, GameSessionKey construction);

}
