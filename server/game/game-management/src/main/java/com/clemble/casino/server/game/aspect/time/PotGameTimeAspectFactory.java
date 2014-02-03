package com.clemble.casino.server.game.aspect.time;

import com.clemble.casino.game.PotGameContext;
import com.clemble.casino.game.construct.GameInitiation;
import com.clemble.casino.game.event.server.GameMatchEndedEvent;
import com.clemble.casino.server.game.aspect.GameAspect;
import com.clemble.casino.server.game.aspect.PotGameAspectFactory;

public class PotGameTimeAspectFactory implements PotGameAspectFactory<GameMatchEndedEvent<?>> {

    @Override
    public GameAspect<GameMatchEndedEvent<?>> construct(GameInitiation initiation, PotGameContext potContext) {
        return new PotGameTimeAspect(potContext);
    }

    @Override
    public int getOrder() {
        return 1;
    }

}
