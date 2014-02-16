package com.clemble.casino.integration.game.construction;

import static com.clemble.casino.utils.Preconditions.checkNotNull;

import java.util.List;

import com.clemble.casino.game.Game;
import com.clemble.casino.game.GameState;
import com.clemble.casino.game.specification.GameConfiguration;
import com.clemble.casino.integration.game.GamePlayer;
import com.clemble.casino.integration.game.MatchGamePlayer;

public class SimpleSyncGameScenarios implements SyncGameScenarios {

    final private GameScenarios gameScenarios;

    public SimpleSyncGameScenarios(GameScenarios gameScenarios) {
        this.gameScenarios = checkNotNull(gameScenarios);
    }
    
    @Override
    public <State extends GameState> List<MatchGamePlayer<State>> match(Game game) {
        List<MatchGamePlayer<State>> players = gameScenarios.<State>match(game);
        // Step 1. Constructing players
        return unite(players);
    }

    @Override
    public <State extends GameState> List<MatchGamePlayer<State>> match(GameConfiguration configuration) {
        // Step 1. Constructing players
        return unite(gameScenarios.<State>match(configuration));
    }

    @Override
    public <P extends GamePlayer> List<P> unite(List<P> players) {
        for(GamePlayer player: players)
            player.addDependent(players);
        return players;
    }

}
