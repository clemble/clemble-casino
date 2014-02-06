package com.clemble.casino.server.game.aspect.time;

import com.clemble.casino.game.PotGameContext;
import com.clemble.casino.game.event.server.GameMatchEndedEvent;
import com.clemble.casino.game.specification.PotGameConfiguration;
import com.clemble.casino.server.game.aspect.GameAspect;
import com.clemble.casino.server.game.aspect.PotGameAspectFactory;

public class PotGameTimeAspectFactory implements PotGameAspectFactory<GameMatchEndedEvent> {

    @Override
    public GameAspect<GameMatchEndedEvent> construct(PotGameConfiguration configuration, PotGameContext potContext) {
        return new PotGameTimeAspect(potContext);
    }

    @Override
    public int getOrder() {
        return 1;
    }

}
