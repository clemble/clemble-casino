package com.clemble.casino.integration.game.construction;

import com.clemble.casino.client.ClembleCasinoOperations;
import com.clemble.casino.game.Game;
import com.clemble.casino.game.GameSessionKey;
import com.clemble.casino.game.GameState;
import com.clemble.casino.game.specification.MatchGameConfiguration;
import com.clemble.casino.integration.game.GameSessionPlayer;

public interface GameScenarios extends BaseGameScenarios {

    public <State extends GameState> GameSessionPlayer<State> construct(Game game, ClembleCasinoOperations initiator);

    public <State extends GameState> GameSessionPlayer<State> construct(MatchGameConfiguration specification, ClembleCasinoOperations initiator);

    public <State extends GameState> GameSessionPlayer<State> construct(Game game, ClembleCasinoOperations initiator, String... participants);

    public <State extends GameState> GameSessionPlayer<State> construct(MatchGameConfiguration specification, ClembleCasinoOperations initiator, String... participants);
    
    public <State extends GameState> GameSessionPlayer<State> construct(GameSessionKey sessionKey, ClembleCasinoOperations player);

    public <State extends GameState> GameSessionPlayer<State> accept(GameSessionKey gameSessionKey, ClembleCasinoOperations participant);

}
