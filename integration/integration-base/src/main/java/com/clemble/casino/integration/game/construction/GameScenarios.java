package com.clemble.casino.integration.game.construction;

import java.util.List;

import com.clemble.casino.game.Game;
import com.clemble.casino.game.GameState;
import com.clemble.casino.game.specification.GameSpecification;
import com.clemble.casino.integration.game.GameSessionPlayer;

public interface GameScenarios {

    public <State extends GameState> List<GameSessionPlayer<State>> constructGame(Game game);

    public <State extends GameState> List<GameSessionPlayer<State>> constructGame(GameSpecification specification);

}
