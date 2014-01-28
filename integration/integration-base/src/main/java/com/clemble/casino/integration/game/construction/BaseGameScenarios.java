package com.clemble.casino.integration.game.construction;

import java.util.List;

import com.clemble.casino.game.Game;
import com.clemble.casino.game.GameState;
import com.clemble.casino.game.specification.MatchGameConfiguration;
import com.clemble.casino.integration.game.GameSessionPlayer;

public interface BaseGameScenarios {

    public <State extends GameState> List<GameSessionPlayer<State>> construct(Game game);

    public <State extends GameState> List<GameSessionPlayer<State>> construct(MatchGameConfiguration specification);

}
