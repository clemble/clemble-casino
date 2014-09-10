package com.clemble.casino.integration.game;

import com.clemble.casino.client.ClembleCasinoOperations;
import com.clemble.casino.game.GameAware;
import com.clemble.casino.game.GameState;
import com.clemble.casino.game.configuration.GameConfiguration;
import com.clemble.casino.game.construction.GameConstruction;

public interface RoundGamePlayerFactory<State extends GameState> extends GameAware {

    public RoundGamePlayer<State> construct(ClembleCasinoOperations player, String construction);

    public RoundGamePlayer<State> construct(ClembleCasinoOperations player, String construction, GameConfiguration configurationKey);

    public RoundGamePlayer<State> construct(ClembleCasinoOperations player, GameConstruction construction);

}
