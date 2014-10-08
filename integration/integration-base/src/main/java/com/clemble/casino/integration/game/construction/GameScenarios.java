package com.clemble.casino.integration.game.construction;

import com.clemble.casino.client.ClembleCasinoOperations;
import com.clemble.casino.game.Game;
import com.clemble.casino.game.lifecycle.configuration.GameConfiguration;
import com.clemble.casino.integration.game.GamePlayer;
import com.clemble.casino.integration.game.RoundGamePlayer;

public interface GameScenarios extends BaseGameScenarios {

    public GamePlayer construct(GameConfiguration configuration, ClembleCasinoOperations player);

    public <T extends RoundGamePlayer<?>> T round(Game game, ClembleCasinoOperations player);

    public <T extends RoundGamePlayer<?>> T round(GameConfiguration configuration, ClembleCasinoOperations player);

    public <T extends RoundGamePlayer<?>> T round(Game game, ClembleCasinoOperations player, String... players);

    public <T extends RoundGamePlayer<?>> T round(GameConfiguration configuration, ClembleCasinoOperations player, String... players);
    
    public <T extends RoundGamePlayer<?>> T round(String sessionKey, ClembleCasinoOperations player);

    public <T extends RoundGamePlayer<?>> T accept(String sessionKey, ClembleCasinoOperations player);

}
