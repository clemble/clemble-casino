package com.clemble.casino.integration.game.construction;

import java.util.List;

import com.clemble.casino.game.Game;
import com.clemble.casino.game.lifecycle.management.GameState;
import com.clemble.casino.game.lifecycle.configuration.GameConfiguration;
import com.clemble.casino.game.lifecycle.configuration.MatchGameConfiguration;
import com.clemble.casino.game.lifecycle.management.RoundState;
import com.clemble.casino.integration.game.MatchGamePlayer;
import com.clemble.casino.integration.game.RoundGamePlayer;

public interface BaseGameScenarios {

    public List<RoundGamePlayer> round(Game game);

    public List<RoundGamePlayer> round(GameConfiguration specification);

    public List<MatchGamePlayer> match();

    public List<MatchGamePlayer> match(MatchGameConfiguration configuration);

}
