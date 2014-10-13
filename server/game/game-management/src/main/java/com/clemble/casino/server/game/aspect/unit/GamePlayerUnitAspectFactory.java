package com.clemble.casino.server.game.aspect.unit;

import com.clemble.casino.game.lifecycle.management.GameContext;
import com.clemble.casino.game.lifecycle.management.GameState;
import com.clemble.casino.game.lifecycle.management.event.action.UseGameUnitAction;
import com.clemble.casino.game.lifecycle.configuration.GameConfiguration;
import com.clemble.casino.lifecycle.management.event.action.PlayerAction;
import com.clemble.casino.server.game.aspect.GameAspect;
import com.clemble.casino.server.game.aspect.GameAspectFactory;
import com.clemble.casino.server.game.aspect.GenericGameAspectFactory;

/**
 * Created by mavarazy on 15/03/14.
 */
public class GamePlayerUnitAspectFactory implements GenericGameAspectFactory<PlayerAction<UseGameUnitAction>> {

    @Override
    public GameAspect<PlayerAction<UseGameUnitAction>> construct(GameConfiguration configuration, GameState state) {
        return new GamePlayerUnitAspect(state.getContext());
    }

    @Override
    public int getOrder() {
        return 0;
    }

}
