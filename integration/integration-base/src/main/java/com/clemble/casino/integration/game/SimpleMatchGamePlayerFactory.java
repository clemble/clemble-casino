package com.clemble.casino.integration.game;

import com.clemble.casino.client.ClembleCasinoOperations;
import com.clemble.casino.game.Game;
import com.clemble.casino.game.GameSessionKey;
import com.clemble.casino.game.GameState;
import com.clemble.casino.game.construct.GameConstruction;
import com.clemble.casino.game.specification.GameConfigurationKey;

public class SimpleMatchGamePlayerFactory<State extends GameState> implements MatchGamePlayerFactory<State> {

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
    public MatchGamePlayer<State> construct(ClembleCasinoOperations player, GameConstruction construction) {
        // Step 1. Processing game session player
        return construct(player, construction.getSession(), construction.getRequest().getConfiguration().getConfigurationKey());
    }

    @Override
    public MatchGamePlayer<State> construct(ClembleCasinoOperations player, GameSessionKey sessionKey) {
        // Step 1. Fetching game construction
        GameConstruction gameConstruction = player.gameConstructionOperations().getConstruct(sessionKey);
        // Step 2. Processing to the generic constructor
        return construct(player, gameConstruction);
    }

    @Override
    public MatchGamePlayer<State> construct(ClembleCasinoOperations player, GameSessionKey sessionKey, GameConfigurationKey configurationKey) {
        return new SimpleMatchGamePlayer(player, sessionKey, configurationKey);
    }

}
