package com.clemble.casino.server.game.aspect.time;

import com.clemble.casino.game.PotGameContext;
import com.clemble.casino.game.event.server.RoundEndedEvent;
import com.clemble.casino.game.specification.PotGameConfiguration;
import com.clemble.casino.server.game.aspect.GameAspect;
import com.clemble.casino.server.game.aspect.PotGameAspectFactory;

public class PotGameTimeAspectFactory implements PotGameAspectFactory<RoundEndedEvent> {

    @Override
    public GameAspect<RoundEndedEvent> construct(PotGameConfiguration configuration, PotGameContext potContext) {
        return new PotGameTimeAspect(potContext);
    }

    @Override
    public int getOrder() {
        return 1;
    }

}
