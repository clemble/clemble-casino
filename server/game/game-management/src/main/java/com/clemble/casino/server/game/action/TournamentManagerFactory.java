package com.clemble.casino.server.game.action;

import com.clemble.casino.game.lifecycle.initiation.GameInitiation;
import com.clemble.casino.game.lifecycle.management.GameContext;
import com.clemble.casino.game.lifecycle.management.TournamentGameState;
import com.clemble.casino.game.lifecycle.management.event.GameManagementEvent;
import com.clemble.casino.server.action.ClembleManager;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * Created by mavarazy on 10/9/14.
 */
public class TournamentManagerFactory implements GameManagerFactory {

    public ClembleManager<GameManagementEvent, TournamentGameState> start(GameInitiation initiation, GameContext<?> parent) {
        throw new NotImplementedException();
    }

}
