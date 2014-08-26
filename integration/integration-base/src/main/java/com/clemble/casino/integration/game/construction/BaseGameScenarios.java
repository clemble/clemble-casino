package com.clemble.casino.integration.game.construction;

import java.util.List;

import com.clemble.casino.game.Game;
import com.clemble.casino.game.GameState;
import com.clemble.casino.game.configuration.GameConfiguration;
import com.clemble.casino.game.configuration.MatchGameConfiguration;
import com.clemble.casino.integration.game.MatchGamePlayer;
import com.clemble.casino.integration.game.RoundGamePlayer;

public interface BaseGameScenarios {

    public <State extends GameState> List<RoundGamePlayer<State>> round(Game game);

    public <State extends GameState> List<RoundGamePlayer<State>> round(GameConfiguration specification);

    public List<MatchGamePlayer> match();

    public List<MatchGamePlayer> match(MatchGameConfiguration configuration);

}
