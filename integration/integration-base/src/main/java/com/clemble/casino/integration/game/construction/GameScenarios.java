package com.clemble.casino.integration.game.construction;

import java.util.List;

import com.clemble.casino.client.ClembleCasinoOperations;
import com.clemble.casino.game.Game;
import com.clemble.casino.game.GameSessionKey;
import com.clemble.casino.game.GameState;
import com.clemble.casino.game.specification.GameSpecification;
import com.clemble.casino.integration.game.GameSessionPlayer;

public interface GameScenarios {

    public <State extends GameState> List<GameSessionPlayer<State>> construct(Game game);

    public <State extends GameState> List<GameSessionPlayer<State>> construct(GameSpecification specification);

    public <State extends GameState> GameSessionPlayer<State> construct(Game game, ClembleCasinoOperations initiator);

    public <State extends GameState> GameSessionPlayer<State> construct(GameSpecification specification, ClembleCasinoOperations initiator);

    public <State extends GameState> GameSessionPlayer<State> construct(Game game, ClembleCasinoOperations initiator, String... participants);

    public <State extends GameState> GameSessionPlayer<State> construct(GameSpecification specification, ClembleCasinoOperations initiator, String... participants);

    public <State extends GameState> GameSessionPlayer<State> accept(GameSessionKey gameSessionKey, ClembleCasinoOperations participant);

}
