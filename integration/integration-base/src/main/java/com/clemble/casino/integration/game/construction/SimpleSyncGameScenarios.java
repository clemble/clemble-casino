package com.clemble.casino.integration.game.construction;

import static com.clemble.casino.utils.Preconditions.checkNotNull;

import java.util.List;

import com.clemble.casino.game.Game;
import com.clemble.casino.game.GameState;
import com.clemble.casino.game.specification.GameSpecification;
import com.clemble.casino.integration.game.GameSessionPlayer;

public class SimpleSyncGameScenarios implements SyncGameScenarios {

    final private GameScenarios gameScenarios;

    public SimpleSyncGameScenarios(GameScenarios gameScenarios) {
        this.gameScenarios = checkNotNull(gameScenarios);
    }
    
    @Override
    public <State extends GameState> List<GameSessionPlayer<State>> construct(Game game) {
        // Step 1. Constructing players
        return unite(gameScenarios.<State>construct(game));
    }

    @Override
    public <State extends GameState> List<GameSessionPlayer<State>> construct(GameSpecification specification) {
        // Step 1. Constructing players
        return unite(gameScenarios.<State>construct(specification));
    }

    @Override
    public <State extends GameState> List<GameSessionPlayer<State>> unite(List<GameSessionPlayer<State>> players) {
        for(GameSessionPlayer<State> player: players)
            player.addDependent(players);
        return players;
    }

}
