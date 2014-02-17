package com.clemble.casino.integration.game.construction;

import java.util.List;

import com.clemble.casino.game.Game;
import com.clemble.casino.game.GameState;
import com.clemble.casino.game.specification.GameConfiguration;
import com.clemble.casino.game.specification.PotGameConfiguration;
import com.clemble.casino.integration.game.MatchGamePlayer;
import com.clemble.casino.integration.game.PotGamePlayer;

public interface BaseGameScenarios {

    public <State extends GameState> List<MatchGamePlayer<State>> match(Game game);

    public <State extends GameState> List<MatchGamePlayer<State>> match(GameConfiguration specification);

    public List<PotGamePlayer> pot();

    public List<PotGamePlayer> pot(PotGameConfiguration configuration);

}
