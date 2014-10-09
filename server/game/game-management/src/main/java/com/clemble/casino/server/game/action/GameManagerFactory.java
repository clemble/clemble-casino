package com.clemble.casino.server.game.action;

import com.clemble.casino.game.lifecycle.initiation.GameInitiation;
import com.clemble.casino.game.lifecycle.management.GameContext;
import com.clemble.casino.game.lifecycle.management.GameState;
import com.clemble.casino.game.lifecycle.management.event.GameManagementEvent;
import com.clemble.casino.lifecycle.management.State;
import com.clemble.casino.server.action.ClembleManager;

/**
 * Created by mavarazy on 10/9/14.
 */
public interface GameManagerFactory {

    public ClembleManager<GameManagementEvent, ? extends State<GameManagementEvent>> start(GameInitiation initiation, GameContext<?> parent);

}
