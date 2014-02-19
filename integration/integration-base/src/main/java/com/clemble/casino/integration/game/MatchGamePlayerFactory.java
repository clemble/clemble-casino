package com.clemble.casino.integration.game;

import com.clemble.casino.client.ClembleCasinoOperations;
import com.clemble.casino.game.GameAware;
import com.clemble.casino.game.GameSessionKey;
import com.clemble.casino.game.GameState;
import com.clemble.casino.game.construct.GameConstruction;
import com.clemble.casino.game.specification.GameConfigurationKey;

public interface MatchGamePlayerFactory<State extends GameState> extends GameAware {

    public MatchGamePlayer<State> construct(ClembleCasinoOperations player, GameSessionKey construction);

    public MatchGamePlayer<State> construct(ClembleCasinoOperations player, GameSessionKey construction, GameConfigurationKey configurationKey);

    public MatchGamePlayer<State> construct(ClembleCasinoOperations player, GameConstruction construction);

}
