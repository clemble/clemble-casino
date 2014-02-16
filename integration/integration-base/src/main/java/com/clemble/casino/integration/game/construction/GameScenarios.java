package com.clemble.casino.integration.game.construction;

import com.clemble.casino.client.ClembleCasinoOperations;
import com.clemble.casino.game.Game;
import com.clemble.casino.game.GameSessionKey;
import com.clemble.casino.game.GameState;
import com.clemble.casino.game.specification.GameConfiguration;
import com.clemble.casino.integration.game.MatchGamePlayer;

public interface GameScenarios extends BaseGameScenarios {

    public <State extends GameState> MatchGamePlayer<State> match(Game game, ClembleCasinoOperations initiator);

    public <State extends GameState> MatchGamePlayer<State> match(GameConfiguration specification, ClembleCasinoOperations initiator);

    public <State extends GameState> MatchGamePlayer<State> match(Game game, ClembleCasinoOperations initiator, String... participants);

    public <State extends GameState> MatchGamePlayer<State> match(GameConfiguration specification, ClembleCasinoOperations initiator, String... participants);
    
    public <State extends GameState> MatchGamePlayer<State> match(GameSessionKey sessionKey, ClembleCasinoOperations player);

    public <State extends GameState> MatchGamePlayer<State> accept(GameSessionKey gameSessionKey, ClembleCasinoOperations participant);

}
