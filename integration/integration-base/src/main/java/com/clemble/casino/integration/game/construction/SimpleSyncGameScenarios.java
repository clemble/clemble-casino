package com.clemble.casino.integration.game.construction;

import static com.clemble.casino.utils.Preconditions.checkNotNull;

import java.util.List;

import com.clemble.casino.game.Game;
import com.clemble.casino.game.lifecycle.configuration.GameConfiguration;
import com.clemble.casino.game.lifecycle.configuration.MatchGameConfiguration;
import com.clemble.casino.integration.game.GamePlayer;
import com.clemble.casino.integration.game.MatchGamePlayer;
import com.clemble.casino.integration.game.RoundGamePlayer;

public class SimpleSyncGameScenarios implements SyncGameScenarios {

    final private GameScenarios gameScenarios;

    public SimpleSyncGameScenarios(GameScenarios gameScenarios) {
        this.gameScenarios = checkNotNull(gameScenarios);
    }
    
    @Override
    public List<RoundGamePlayer> round(Game game) {
        List<RoundGamePlayer> players = gameScenarios.round(game);
        // Step 1. Constructing players
        return unite(players);
    }

    @Override
    public List<RoundGamePlayer> round(GameConfiguration configuration) {
        // Step 1. Constructing players
        return unite(gameScenarios.round(configuration));
    }

    @Override
    public <P extends GamePlayer> List<P> unite(List<P> players) {
        for(GamePlayer player: players)
            player.addDependent(players);
        return players;
    }

    @Override
    public List<MatchGamePlayer> match() {
        List<MatchGamePlayer> players = gameScenarios.match();
        // Step 1. Constructing players
        return unite(players);
    }

    @Override
    public List<MatchGamePlayer> match(MatchGameConfiguration configuration) {
        List<MatchGamePlayer> players = gameScenarios.match(configuration);
        // Step 1. Constructing players
        return unite(players);
    }

}
