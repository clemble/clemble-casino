package com.clemble.casino.server.game.action;

import com.clemble.casino.game.lifecycle.initiation.GameInitiation;
import com.clemble.casino.game.lifecycle.management.*;
import com.clemble.casino.game.lifecycle.management.event.GameManagementEvent;
import com.clemble.casino.server.action.ClembleManager;

public interface GameManagerFactory {

    public ClembleManager<GameManagementEvent, ? extends GameState> start(GameInitiation initiation, GameContext<?> parent);

}
