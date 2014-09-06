package com.clemble.casino.server.game.aspect.unit;

import com.clemble.casino.game.GameContext;
import com.clemble.casino.game.action.UseGameUnitEvent;
import com.clemble.casino.game.configuration.GameConfiguration;
import com.clemble.casino.server.game.aspect.GameAspect;
import com.clemble.casino.server.game.aspect.GameAspectFactory;

/**
 * Created by mavarazy on 15/03/14.
 */
public class GamePlayerUnitAspectFactory implements GameAspectFactory<UseGameUnitEvent, GameContext<?>, GameConfiguration>{

    @Override
    public GameAspect<UseGameUnitEvent> construct(GameConfiguration configuration, GameContext<?> context) {
        return new GamePlayerUnitAspect(context);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
