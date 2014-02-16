package com.clemble.casino.integration.game.construction;

import java.util.List;

import com.clemble.casino.integration.game.GamePlayer;

public interface SyncGameScenarios extends BaseGameScenarios {

    public <P extends GamePlayer> List<P> unite(List<P> players);

}
