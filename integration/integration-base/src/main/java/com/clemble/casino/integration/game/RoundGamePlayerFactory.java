package com.clemble.casino.integration.game;

import com.clemble.casino.client.ClembleCasinoOperations;
import com.clemble.casino.game.GameAware;
import com.clemble.casino.game.GameState;
import com.clemble.casino.game.construct.GameConstruction;

public interface RoundGamePlayerFactory<State extends GameState> extends GameAware {

    public RoundGamePlayer<State> construct(ClembleCasinoOperations player, String construction);

    public RoundGamePlayer<State> construct(ClembleCasinoOperations player, String construction, String configurationKey);

    public RoundGamePlayer<State> construct(ClembleCasinoOperations player, GameConstruction construction);

}
