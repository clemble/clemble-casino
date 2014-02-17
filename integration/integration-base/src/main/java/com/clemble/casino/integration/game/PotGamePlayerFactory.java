package com.clemble.casino.integration.game;

import com.clemble.casino.client.ClembleCasinoOperations;
import com.clemble.casino.game.GameSessionKey;
import com.clemble.casino.game.construct.GameConstruction;

public class PotGamePlayerFactory {

    public PotGamePlayer construct(ClembleCasinoOperations player, GameConstruction construction) {
        return new SimplePotGamePlayer(player, construction);
    }

    public PotGamePlayer construct(ClembleCasinoOperations player, GameSessionKey sessionKey) {
        // Step 1. Fetching game construction
        GameConstruction construction = player.gameConstructionOperations().getConstruct(sessionKey);
        return new SimplePotGamePlayer(player, construction);
    }
}
