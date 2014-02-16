package com.clemble.casino.integration.game;

import com.clemble.casino.client.ClembleCasinoOperations;
import com.clemble.casino.game.Game;
import com.clemble.casino.game.GameSessionKey;
import com.clemble.casino.game.GameState;
import com.clemble.casino.game.construct.GameConstruction;

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
        // Step 1. Fetching session key
        GameSessionKey sessionKey = construction.getSession();
        // Step 2. Processing game session player
        return (MatchGamePlayer<State>) new SimpleMatchGamePlayer<>(player, construction, player.gameActionOperations(sessionKey));
    }

    @Override
    public MatchGamePlayer<State> construct(ClembleCasinoOperations player, GameSessionKey sessionKey) {
        // Step 1. Fetching game construction
        GameConstruction gameConstruction = player.gameConstructionOperations(sessionKey.getGame()).getConstruct(sessionKey.getSession());
        // Step 2. Processing to the generic constructor
        return construct(player, gameConstruction);
    }

}
