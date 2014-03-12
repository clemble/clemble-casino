package com.clemble.casino.integration.game.construction;

import static com.clemble.casino.utils.Preconditions.checkNotNull;

import java.util.List;

import com.clemble.casino.game.Game;
import com.clemble.casino.game.GameState;
import com.clemble.casino.game.specification.GameConfiguration;
import com.clemble.casino.game.specification.MatchGameConfiguration;
import com.clemble.casino.integration.game.GamePlayer;
import com.clemble.casino.integration.game.MatchGamePlayer;
import com.clemble.casino.integration.game.RoundGamePlayer;

public class SimpleSyncGameScenarios implements SyncGameScenarios {

    final private GameScenarios gameScenarios;

    public SimpleSyncGameScenarios(GameScenarios gameScenarios) {
        this.gameScenarios = checkNotNull(gameScenarios);
    }
    
    @Override
    public <State extends GameState> List<RoundGamePlayer<State>> round(Game game) {
        List<RoundGamePlayer<State>> players = gameScenarios.<State>round(game);
        // Step 1. Constructing players
        return unite(players);
    }

    @Override
    public <State extends GameState> List<RoundGamePlayer<State>> round(GameConfiguration configuration) {
        // Step 1. Constructing players
        return unite(gameScenarios.<State>round(configuration));
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
