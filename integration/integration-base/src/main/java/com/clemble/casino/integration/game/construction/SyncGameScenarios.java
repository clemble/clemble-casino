package com.clemble.casino.integration.game.construction;

import java.util.List;

import com.clemble.casino.game.GameState;
import com.clemble.casino.integration.game.GameSessionPlayer;

public interface SyncGameScenarios extends BaseGameScenarios {

    public <State extends GameState> List<GameSessionPlayer<State>> unite(List<GameSessionPlayer<State>> players);

}
