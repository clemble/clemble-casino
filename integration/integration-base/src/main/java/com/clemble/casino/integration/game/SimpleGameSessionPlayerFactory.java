package com.clemble.casino.integration.game;

import com.clemble.casino.game.GameSessionKey;
import com.clemble.casino.game.GameState;
import com.clemble.casino.game.construct.GameConstruction;
import com.clemble.casino.integration.player.Player;

public class SimpleGameSessionPlayerFactory implements GameSessionPlayerFactory {

    public SimpleGameSessionPlayerFactory() {
    }

    @Override
    @SuppressWarnings("unchecked")
    public <State extends GameState> GameSessionPlayer<State> construct(Player player, GameConstruction construction) {
        // Step 1. Fetching session key
        GameSessionKey sessionKey = construction.getSession();
        // Step 2. Processing game session player
        return (GameSessionPlayer<State>) new SimpleGameSessionPlayer<>(player, construction, player.gameConstructionOperations(sessionKey.getGame()).getActionOperations(sessionKey.getSession()));
    }

    @Override
    public <State extends GameState> GameSessionPlayer<State> construct(Player player, GameSessionKey sessionKey) {
        // Step 1. Fetching game construction
        GameConstruction gameConstruction = player.gameConstructionOperations(sessionKey.getGame()).getConstruct(sessionKey.getSession());
        // Step 2. Processing to the generic constructor
        return construct(player, gameConstruction);
    }

}
