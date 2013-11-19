package com.clemble.casino.integration.game.construction;

import java.util.List;

import com.clemble.casino.game.Game;
import com.clemble.casino.game.GameSessionKey;
import com.clemble.casino.game.GameState;
import com.clemble.casino.game.specification.GameSpecification;
import com.clemble.casino.integration.game.GameSessionPlayer;
import com.clemble.casino.integration.player.Player;

public interface GameScenarios {

    public <State extends GameState> List<GameSessionPlayer<State>> construct(Game game);

    public <State extends GameState> List<GameSessionPlayer<State>> construct(GameSpecification specification);

    public <State extends GameState> GameSessionPlayer<State> construct(Game game, Player initiator);

    public <State extends GameState> GameSessionPlayer<State> construct(GameSpecification specification, Player initiator);

    public <State extends GameState> GameSessionPlayer<State> construct(Game game, Player initiator, String... participants);

    public <State extends GameState> GameSessionPlayer<State> construct(GameSpecification specification, Player initiator, String... participants);

    public <State extends GameState> GameSessionPlayer<State> accept(GameSessionKey gameSessionKey, Player participant);

}
