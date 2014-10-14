package com.clemble.casino.integration.game;

import com.clemble.casino.client.ClembleCasinoOperations;
import com.clemble.casino.game.GameAware;
import com.clemble.casino.game.lifecycle.management.GameState;
import com.clemble.casino.game.lifecycle.configuration.GameConfiguration;
import com.clemble.casino.game.lifecycle.construction.GameConstruction;
import com.clemble.casino.game.lifecycle.management.RoundState;

public interface RoundGamePlayerFactory extends GameAware {

    public RoundGamePlayer construct(ClembleCasinoOperations player, String construction);

    public RoundGamePlayer construct(ClembleCasinoOperations player, String construction, GameConfiguration configurationKey);

    public RoundGamePlayer construct(ClembleCasinoOperations player, GameConstruction construction);

}
