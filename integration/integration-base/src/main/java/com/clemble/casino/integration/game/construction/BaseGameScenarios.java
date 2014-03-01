package com.clemble.casino.integration.game.construction;

import java.util.List;

import com.clemble.casino.game.Game;
import com.clemble.casino.game.GameState;
import com.clemble.casino.game.specification.GameConfiguration;
import com.clemble.casino.game.specification.MatchGameConfiguration;
import com.clemble.casino.integration.game.MatchGamePlayer;
import com.clemble.casino.integration.game.RoundGamePlayer;

public interface BaseGameScenarios {

    public <State extends GameState> List<RoundGamePlayer<State>> match(Game game);

    public <State extends GameState> List<RoundGamePlayer<State>> match(GameConfiguration specification);

    public List<MatchGamePlayer> pot();

    public List<MatchGamePlayer> pot(MatchGameConfiguration configuration);

}
