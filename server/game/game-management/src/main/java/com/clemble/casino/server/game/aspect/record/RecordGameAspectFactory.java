package com.clemble.casino.server.game.aspect.record;

import com.clemble.casino.event.GameEvent;
import com.clemble.casino.game.GameContext;
import com.clemble.casino.game.specification.GameConfiguration;
import com.clemble.casino.server.game.aspect.GameAspect;
import com.clemble.casino.server.game.aspect.GameAspectFactory;

/**
 * Created by mavarazy on 10/03/14.
 */
public class RecordGameAspectFactory implements GameAspectFactory<GameEvent> {

    @Override
    public GameAspect<GameEvent> construct(GameConfiguration configuration, GameContext potContext) {
        return null;
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
