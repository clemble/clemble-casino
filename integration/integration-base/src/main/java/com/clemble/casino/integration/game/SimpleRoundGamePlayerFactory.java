package com.clemble.casino.integration.game;

import com.clemble.casino.client.ClembleCasinoOperations;
import com.clemble.casino.game.Game;
import com.clemble.casino.game.lifecycle.configuration.GameConfiguration;
import com.clemble.casino.game.lifecycle.construction.GameConstruction;

public class SimpleRoundGamePlayerFactory implements RoundGamePlayerFactory {

    /**
     * Generate 19/11/12
     */
    private static final long serialVersionUID = -7001333411232068261L;

    @Override
    public Game getGame() {
        return Game.num;
    }

    @Override
    @SuppressWarnings("unchecked")
    public RoundGamePlayer construct(ClembleCasinoOperations player, GameConstruction construction) {
        // Step 1. Processing game session player
        return construct(player, construction.getSessionKey(), construction.getConfiguration());
    }

    @Override
    public RoundGamePlayer construct(ClembleCasinoOperations player, String sessionKey) {
        // Step 1. Fetching game construction
        GameConstruction gameConstruction = player.gameConstructionOperations().getConstruct(sessionKey);
        // Step 2. Processing to the generic constructor
        return construct(player, gameConstruction);
    }

    @Override
    public RoundGamePlayer construct(ClembleCasinoOperations player, String sessionKey, GameConfiguration configuration) {
        return new SimpleRoundGamePlayer(player, sessionKey, configuration);
    }

}
